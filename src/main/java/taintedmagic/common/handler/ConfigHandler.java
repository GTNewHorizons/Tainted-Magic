package taintedmagic.common.handler;

import net.minecraftforge.common.config.Configuration;

import taintedmagic.common.TaintedMagic;

public class ConfigHandler {

    public static Configuration config;

    public static boolean useUpdateHandler = true;
    public static boolean researchTags = true;
    public static boolean useCustomResearchTabBackground = false;
    public static int magesMaceBaseDamage;
    public static float magesMaceStaffMultiple;
    public static int eldritchBaseDamage;
    public static float eldritchStaffMultiple;
    public static int taintStormBaseDamage;
    public static float taintStormStaffMultiple;
    public static int taintedBlastBaseDamage;
    public static float taintedBlastStaffMultiple;
    public static float taintedBlastReclining;
    public static int visShardBaseDamage;
    public static float visShardStaffMultiple;

    public static void init() {
        TaintedMagic.log.info("Loading config");

        config.load();

        useUpdateHandler = config
                .getBoolean("use_update_handler", "misc", true, "Should update notifications be enabled?");
        researchTags = config.getBoolean(
                "research_tags",
                "research",
                true,
                "Setting this to false will disable the '[TaintedMagic]' tag on the research");
        useCustomResearchTabBackground = config.getBoolean(
                "use_custom_research_tab_background",
                "research",
                false,
                "Setting this to true will enable the old custom tab background");

        /* FOCUS */
        magesMaceBaseDamage = config
                .getInt("mages_mace_base_damage", "foci", 60, 1, 3000, "Set damage for Mage's Mace focus.");
        magesMaceStaffMultiple = config.getFloat(
                "mages_mace_staff_multiplier",
                "foci",
                2f,
                1,
                10,
                "Set damage multiplier for Staff with Mage's Mace focus.");
        eldritchBaseDamage = config
                .getInt("eldritch_base_damage", "foci", 150, 1, 3000, "Set damage for Dark Matter focus.");
        eldritchStaffMultiple = config.getFloat(
                "eldritch_staff_multiplier",
                "foci",
                1.5f,
                1,
                10,
                "Set damage multiplier for Staff with Dark Matter focus.");
        taintStormBaseDamage = config
                .getInt("taint_storm_damage", "foci", 15, 1, 3000, "Set damage for Tainted Storm focus.");
        taintStormStaffMultiple = config.getFloat(
                "taint_storm_damage_staff_multiplier",
                "foci",
                1.5f,
                1,
                10,
                "Set damage multiplier for Staff with Tainted Storm focus.");
        taintedBlastBaseDamage = config
                .getInt("tainted_blast_damage", "foci", 75, 1, 3000, "Set damage for Tainted Shockwave focus.");
        taintedBlastStaffMultiple = config.getFloat(
                "tainted_blast_staff_multiplier",
                "foci",
                1.5f,
                1,
                10,
                "Set damage multiplier for Staff with Tainted Shockwave focus.");
        taintedBlastReclining = config
                .getInt(" tainted_blast_reclining", "foci", 8, 1, 12, "Set reclining for Tainted Shockwave focus.");
        visShardBaseDamage = config.getInt("vis_hard_damage", "foci", 35, 1, 3000, "Set damage for Vis Shard focus.");
        visShardStaffMultiple = config.getFloat(
                "vis_hard_staff_multiplier",
                "foci",
                1.5f,
                1,
                10,
                "Set damage multiplier for Staff with Vis Shard focus.");
        /* END */

        config.save();
    }
}
