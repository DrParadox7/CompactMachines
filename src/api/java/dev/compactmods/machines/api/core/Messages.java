package dev.compactmods.machines.api.core;

import net.minecraft.resources.ResourceLocation;

public abstract class Messages {
    public static final ResourceLocation CANNOT_ENTER_MACHINE = new ResourceLocation(Constants.MOD_ID, "cannot_enter");
    public static final ResourceLocation NO_MACHINE_DATA = new ResourceLocation(Constants.MOD_ID, "no_machine_data");
    public static final ResourceLocation MACHINE_SPAWNPOINT_SET = new ResourceLocation(Constants.MOD_ID, "spawnpoint_set");
    public static final ResourceLocation TELEPORT_OUT_OF_BOUNDS = new ResourceLocation(Constants.MOD_ID, "teleport_oob");
    public static final ResourceLocation HOW_DID_YOU_GET_HERE = new ResourceLocation(Constants.MOD_ID, "how_did_you_get_here");
    public static final ResourceLocation FIXBIOME_IN_BAD_DIMENSION = new ResourceLocation(Constants.MOD_ID, "fixbiome_bad_dim");
    public static final ResourceLocation UNKNOWN_TUNNEL = new ResourceLocation(Constants.MOD_ID, "unknown_tunnel_type");
}
