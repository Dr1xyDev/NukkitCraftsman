package cn.nukkit.level.generator.populator;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.math.NukkitRandom;

/**
 * Populator that generates lava lakes and lava falls in the Nether
 */
public class PopulatorNetherLava extends Populator {

    private int baseAmount = 5;

    @Override
    public void populate(ChunkManager level, int chunkX, int chunkZ, NukkitRandom random) {
        int amount = random.nextBoundedInt(10) < this.baseAmount ? 1 : 0;

        for (int i = 0; i < amount; i++) {
            int x = NukkitMath.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
            int z = NukkitMath.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
            int y = NukkitMath.randomRange(random, 5, 30);

            // Create a lava lake
            int radius = random.nextBoundedInt(4) + 2;
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        double dist = Math.sqrt(dx * dx + dz * dz);
                        if (dist <= radius && y + dy >= 1 && y + dy < 127) {
                            int bx = x + dx;
                            int bz = z + dz;
                            int by = y + dy;
                            int blockId = level.getBlockIdAt(bx, by, bz);
                            if (blockId == Block.NETHERRACK || blockId == Block.AIR) {
                                if (dy == -1 || (dist <= radius - 1 && dy == 0)) {
                                    level.setBlockIdAt(bx, by, bz, Block.STILL_LAVA);
                                }
                            }
                        }
                    }
                }
            }

            // Occasionally add a lava waterfall from ceiling
            if (random.nextBoundedInt(3) == 0) {
                int fallX = NukkitMath.randomRange(random, chunkX << 4, (chunkX << 4) + 15);
                int fallZ = NukkitMath.randomRange(random, chunkZ << 4, (chunkZ << 4) + 15);
                for (int fallY = 100; fallY > y; fallY--) {
                    int fallBlock = level.getBlockIdAt(fallX, fallY, fallZ);
                    if (fallBlock == Block.AIR) {
                        level.setBlockIdAt(fallX, fallY, fallZ, Block.STILL_LAVA);
                    } else if (fallBlock == Block.NETHERRACK) {
                        break;
                    }
                }
            }
        }
    }

    public void setBaseAmount(int amount) {
        this.baseAmount = amount;
    }
}
