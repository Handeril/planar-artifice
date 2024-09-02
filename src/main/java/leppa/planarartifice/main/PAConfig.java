package leppa.planarartifice.main;

import net.minecraftforge.common.config.Config;

@Config(modid = PlanarArtifice.MODID)
public class PAConfig {
    @Config.Ignore
    public static final String key = PlanarArtifice.MODID + ".config.";
    public static Compat compat = new Compat();
    public static Balance balance = new Balance(); // new balance lmao
    public static Client client = new Client();

    public static class Compat {
        @Config.Ignore
        public static final String key = PAConfig.key + "compat.";
        @Config.LangKey(key + "tacompat")
        @Config.Name("Disable Thaumic Addition Compat")
        public boolean disableTACompat = false;
        @Config.LangKey(key + "taextracompat")
        @Config.Name("Disable Thaumic Addition Extra Compat")
        public boolean disableTAExtraCompat = false;
        @Config.LangKey(key + "tinkerscompat")
        @Config.Name("Disable Tinkers Construct Compat")
        public boolean disableTinkersCompat = false;
        @Config.LangKey(key + "aspectcompat")
        @Config.Name("Disable Modded Aspect in Random Crystal Crafting")
        public boolean disableAspectCompat = false;
        @Config.LangKey(key + "xercapaintcompat")
        @Config.Name("Disable Joy of Painting Compat")
        public boolean disableXercaPaintCompat = false;
    }

    public static class Balance {
        @Config.Ignore
        public static final String key = PAConfig.key + "balance.";
        @Config.LangKey(key + "taint_feature_gen")
        @Config.Name("Taint Feature Generation Rate")
        @Config.RangeDouble(min = 0, max = 1)
        public double taintFeatureGenRate = 0.05;
        @Config.LangKey(key + "centrifuge_flux_rate")
        @Config.Name("Centrifuge Flux Rate")
        @Config.RangeDouble(min = 0, max = 1)
        public double centrifugeFluxRate = 0.25;
    }

    public static class Client {
        @Config.LangKey(key + "knowledge_icons_per_row")
        @Config.Name("Knowledge Icons per Row")
        @Config.RangeInt(min = 1)
        public int knowledgeIconsPerRow = 7;
    }
}