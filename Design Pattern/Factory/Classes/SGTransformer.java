
public class SGTransformer extends CB2ATransformer { // CB2ATransformer  extends Transformer

    protected String getSequencer() {
        return "SOCIETE_GENERALE";
    }

    @Override
    protected CB2AValidationInfo getValidationInfo() {
        return new SgValidationInfo();
    }

    @Override
    protected String getCDKTransformerName() {
        return "SOCIETE_GENERALE";
    }

}
