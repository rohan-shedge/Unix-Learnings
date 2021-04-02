public class TransformerFactory
{

    private static TransformerFactory INSTANCE = new TransformerFactory();
    
    private static Map<String, Class<? extends Transformer>> TRANSFORMERS_MAP = new HashMap<String, Class<? extends Transformer>>();
    
    private TransformerFactory()
    {
    }
    static 
    {
        //There are 200 connectors, Hence better the map the class object rather than creating the object
        TRANSFORMERS_MAP.put("BNP", ag.payon.bfpe.service.transform.format.cb2a.BnpTransformer.class);
        TRANSFORMERS_MAP.put("BNPUK", ag.payon.bfpe.service.transform.format.cb2a.BnpUkTransformer.class);
        TRANSFORMERS_MAP.put("SOCIETE_GENERALE", ag.payon.bfpe.service.transform.format.cb2a.SgTransformer.class);
        TRANSFORMERS_MAP.put("CMCIC", ag.payon.bfpe.service.transform.format.cb2a.CmcicTransformer.class);
    }
    
    public static TransformerFactory get()
    {
        return INSTANCE;
    }
    
    public Transformer createTransformer(String _type)
    {
        Transformer result = null;
        Class<? extends Transformer> cls = TRANSFORMERS_MAP.get(_type);
        if (cls != null)
        {
            result = ClsUtil.createType(cls);// result = _type.newInstance()
        }
        if (result == null)
        {
            LOGGER.fatal("could not load transformer for: " + _type);
        }
        return result;
    }
}    