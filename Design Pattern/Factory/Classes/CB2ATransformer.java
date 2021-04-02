
public abstract class CB2ATransformer extends Transformer {

     protected abstract String getSequencer();

    protected abstract CB2AValidationInfo getValidationInfo();


    public int getTransactionsCount() {
        //fetch from DB
    }

  
    protected int getTxCountPerBaseRec() {
        return TX_PER_FILE;
    }

    @Override
    public BaseFile transformInternal() {
        int transactionsInBfc = getTransactionsCount();
        BaseFile resultFile = createFile();

        int fileNr = 1;

        int txTransformCount = getTxCountPerBaseRec();
        if (txTransformCount <= 0) {
            txTransformCount = transactionsInBfc;
        }

        txTransformCount = Math.min(txTransformCount, transactionsInBfc);
        int unprocessedLeft = transactionsInBfc;
        int page = 0;
        int initialStart = 0;
        while (unprocessedLeft > 0) {
            String cdkResult = null;
            try {
                LOGGER.info("Transforming " + txTransformCount + " transaction starting with " + initialStart);
                cdkResult = transformTransactions(initialStart, txTransformCount);
                page++;
                initialStart = page * txTransformCount;
            } catch (IOException e) {
                LOGGER.error("transaction cannot be transformed to csv", e);
                return null;
            }

            if( isCDKErrorResponse(cdkResult) )
            {
                LOGGER.error("file generation fails because of cdk error response " + cdkResult);
                return null;
            }

            LOGGER.info("adding " + txTransformCount + " transactions in bank file");
            addBaseRecToFile(resultFile, fileNr++, cdkResult);

            unprocessedLeft -= txTransformCount;
            if (txTransformCount > unprocessedLeft) {
                txTransformCount = unprocessedLeft;
            }
        }

        return resultFile;
    }

    protected int getDBFetchCount(int _count) {
        return Math.min(_count, DBFETCH_COUNT);
    }

 
    protected String transformTransactions(int initialStart, int count) throws IOException {
        int fetchCount = getDBFetchCount(count);
        int unprocessedLeft = count;
        int bias = 0;

        List<Transaction> allTxList = new LinkedList<Transaction>();

        while (unprocessedLeft > 0) {
            int startIndex = initialStart + bias * fetchCount;
            int bucketSize = Math.min(fetchCount, unprocessedLeft);
            List<Transaction> txList = loadTransactions(startIndex, bucketSize);
            LOGGER.info("loaded batch of " + txList.size() + " transactions for transformation");
            allTxList.addAll(txList);
            unprocessedLeft -= txList.size();
            bias++;
        }

        //Adapting the List<Transaction>  to TransformerRequest
        //transform the transactions from TransformerRequest to cbcomPackage
        //setting validation information for the received file
        return cdkResult;
    }

 }