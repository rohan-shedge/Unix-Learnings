
public abstract class Transformer
{

    protected abstract BaseFile transformInternal();
    
   
    protected String getCDKTransformerName() 
    {
        return "";
    }

    public BaseFile transform()
    {
        try
        {
            if( getBankFileControl() != null) 
            {
                int txSize = getBankFileControl().getTxCount(TxProcessingStatus.INCLUDED, bfc_.getTestAdvise());
                LOGGER.info("BFC contains " + txSize + " transactions");
                if( txSize > 0 ) 
                {
                    BaseFile transformedFile = transformInternal();
                    logFileInfo(txSize, transformedFile);
                    return transformedFile;
                }
                LOGGER.error( "could not transform BFC" + getBankFileControl() + 
                    " because tx list is empty (check previous log entries for errors, probably transactions cannot be deserialized from XML).");
            }
        }
        catch (Exception ex)
        {
            LOGGER.error( "could not transform BFC" + getBankFileControl(), ex );
        }
        return null;
    }

    //many concrete methods here
 
}
