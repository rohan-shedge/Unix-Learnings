public class BNPTransformer extends CB2ATransformer { // CB2ATransformer  extends Transformer

    @Override
    protected String getSequencer() {
        return "BNP";
    }

    @Override
    protected CB2AValidationInfo getValidationInfo() {
        return new BnpValidationInfo();
    }

    @Override
    protected String getCDKTransformerName() {
        return "BNP";
    }
}
