package dev.zprestige.mud.events.impl.player;

import dev.zprestige.mud.events.bus.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BreakBlockEvent extends Event {
    private final BlockPos pos;
    private final Direction enumFacing;

    public BreakBlockEvent(BlockPos pos, Direction enumFacing) {
        this.pos = pos;
        this.enumFacing = enumFacing;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Direction getEnumFacing() {
        return enumFacing;
    }
}
