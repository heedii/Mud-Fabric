package dev.zprestige.mud.manager;

import dev.zprestige.mud.Mud;
import dev.zprestige.mud.util.MC;
import dev.zprestige.mud.util.impl.BlockUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;

public class HoleManager implements MC {
    private ArrayList<HolePos> holes = new ArrayList<>();
    public String time;
    private final Vec3i[] Hole = {
            new Vec3i(1, 0, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1)
    };

    private final Vec3i[] DoubleHoleNorth = {
            new Vec3i(0, 0, -2),
            new Vec3i(-1, 0, -1),
            new Vec3i(1, 0, -1),
            new Vec3i(0, -1, -1),
            new Vec3i(0, -1, 0),
            new Vec3i(-1, 0, 0),
            new Vec3i(1, 0, 0),
            new Vec3i(0, 0, 1)
    };

    private final Vec3i[] DoubleHoleWest = {
            new Vec3i(-2, 0, 0),
            new Vec3i(-1, 0, 1),
            new Vec3i(-1, 0, -1),
            new Vec3i(-1, -1, 0),
            new Vec3i(0, -1, 0),
            new Vec3i(0, 0, 1),
            new Vec3i(0, 0, -1),
            new Vec3i(1, 0, 0)
    };

    public void loadHoles(final int range) {
        if (mc.world == null || mc.player == null) {
            return;
        }
        final long sys = System.currentTimeMillis();
        holes = findHoles(range);
        time = System.currentTimeMillis() - sys + "ms";
    }

    public boolean holeManagerContains(final BlockPos pos) {
        return Mud.holeManager.getHoles().stream().anyMatch(holePos -> holePos.getPos().equals(pos));
    }

    private ArrayList<HolePos> findHoles(final int range) {
        final ArrayList<HolePos> holes = new ArrayList<>();
        if (mc.player == null || mc.world == null) {
            return holes;
        }
        for (final BlockPos pos : BlockUtil.getBlocksInRadius(range)) {
            try {
                final HolePos holePos = getHolePos(pos);
                if (holePos != null) {
                    holes.add(holePos);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return holes;
    }

    public HolePos getHolePos(final BlockPos pos) {
        if (mc.player == null || mc.world == null) {
            return null;
        }
        if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
            return null;
        }
        if (enter(pos)) {
            boolean isSafe = true;
            for (Vec3i vec3i : Hole) {
                if (notSafe(pos.add(vec3i))) {
                    isSafe = false;
                }
            }
            if (isSafe) {
                return new HolePos(pos, Type.Bedrock);
            }
            boolean isUnsafe = true;
            for (Vec3i vec3i : Hole) {
                if (notUnsafe(pos.add(vec3i))) {
                    isUnsafe = false;
                }
            }
            if (isUnsafe) {
                return new HolePos(pos, Type.Obsidian);
            }
            boolean isSafeDoubleNorth = true;
            for (Vec3i vec3i : DoubleHoleNorth) {
                if (notSafe(pos.add(vec3i))) {
                    isSafeDoubleNorth = false;
                }
            }
            if (isSafeDoubleNorth) {
                return new HolePos(pos, Type.DoubleBedrockNorth);

            }
            boolean isUnSafeDoubleNorth = true;
            for (Vec3i vec3i : DoubleHoleNorth) {
                if (notUnsafe(pos.add(vec3i))) {
                    isUnSafeDoubleNorth = false;
                }
            }
            if (isUnSafeDoubleNorth) {
                return new HolePos(pos, Type.DoubleObsidianNorth);

            }
            boolean isSafeDoubleWest = true;
            for (Vec3i vec3i : DoubleHoleWest) {
                if (notUnsafe(pos.add(vec3i))) {
                    isSafeDoubleWest = false;
                }
            }
            if (isSafeDoubleWest) {
                return new HolePos(pos, Type.DoubleBedrockWest);

            }
            boolean isUnSafeDoubleWest = true;
            for (Vec3i vec3i : DoubleHoleWest) {
                if (notUnsafe(pos.add(vec3i))) {
                    isUnSafeDoubleWest = false;
                }
            }
            if (isUnSafeDoubleWest) {
                return new HolePos(pos, Type.DoubleObsidianWest);
            }
        }
        return null;
    }

    private boolean enter(BlockPos pos) {
        if (pos == null){
            return false;
        }
        return mc.world != null && mc.world.getBlockState(pos.up()).getBlock().equals(Blocks.AIR) && mc.world.getBlockState(pos.up().up()).getBlock().equals(Blocks.AIR);
    }

    private boolean notUnsafe(BlockPos pos) {
        if (pos == null || mc.world == null){
            return false;
        }
        return  !mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK) && !mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN);
    }

    private boolean notSafe(BlockPos pos) {
        if (pos == null){
            return false;
        }
        return mc.world != null && !mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK);
    }


    public List<HolePos> getHoles() {
        return holes;
    }

    public static class HolePos {
        private final BlockPos pos;
        private final Type holeType;

        public HolePos(BlockPos pos, Type holeType) {
            this.pos = pos;
            this.holeType = holeType;
        }

        public BlockPos getPos() {
            return pos;
        }

        public Type getHoleType() {
            return holeType;
        }

        public boolean isBedrock() {
            return holeType.equals(Type.Bedrock) || holeType.equals(Type.DoubleBedrockWest) || holeType.equals(Type.DoubleBedrockNorth);
        }

        public boolean isWestDouble() {
            return holeType.equals(Type.DoubleBedrockWest) || holeType.equals(Type.DoubleObsidianWest);
        }

        public boolean isDouble() {
            return holeType.equals(Type.DoubleBedrockNorth) || holeType.equals(Type.DoubleBedrockWest) || holeType.equals(Type.DoubleObsidianNorth) || holeType.equals(Type.DoubleObsidianWest);
        }
    }

    public enum Type {
        Bedrock,
        Obsidian,
        DoubleBedrockNorth,
        DoubleBedrockWest,
        DoubleObsidianNorth,
        DoubleObsidianWest
    }
}