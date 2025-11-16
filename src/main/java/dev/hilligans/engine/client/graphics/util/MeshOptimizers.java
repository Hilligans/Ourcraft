package dev.hilligans.engine.client.graphics.util;

import dev.hilligans.engine.client.graphics.api.IMeshOptimizer;
import dev.hilligans.engine.client.graphics.resource.VertexFormat;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.resource.BufferAllocator;
import dev.hilligans.engine.resource.IAllocator;
import dev.hilligans.engine.resource.IBufferAllocator;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.meshoptimizer.MeshOptimizer;
import org.lwjgl.util.meshoptimizer.MeshoptOverdrawStatistics;
import org.lwjgl.util.meshoptimizer.MeshoptStream;
import org.lwjgl.util.meshoptimizer.MeshoptVertexCacheStatistics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.util.meshoptimizer.MeshOptimizer.meshopt_analyzeVertexCache;

public class MeshOptimizers {

    public static final IMeshOptimizer optimizer = new IMeshOptimizer() {
        @Override
        public long optimize(VertexFormat vertexFormat, ByteBuffer vertices, ByteBuffer indices, IBufferAllocator allocator) {
            MeshoptStream.Buffer streams = MeshoptStream.create(1)
                    .apply(0, it -> it
                            .data(vertices)
                            .size(vertexFormat.getStride())
                            .stride(vertexFormat.getStride()));

            long vertexCount = vertices.capacity() / vertexFormat.getStride();

            IntBuffer remap = MemoryUtil.memAllocInt((int) vertexCount);

            long uniqueVertexCount = MeshOptimizer.meshopt_generateVertexRemapMulti(remap, indices.asIntBuffer(), vertexCount, streams);
            remap(vertices, indices, remap, vertexFormat.getStride());

            if (uniqueVertexCount < remap.remaining()) {
                remap.limit((int) uniqueVertexCount);

                vertices.limit((int) (uniqueVertexCount * vertexFormat.getStride()));
            }

            MeshOptimizer.meshopt_optimizeVertexCache(indices.asIntBuffer(), indices.asIntBuffer(), uniqueVertexCount);
            MeshOptimizer.meshopt_optimizeOverdraw(indices.asIntBuffer(), indices.asIntBuffer(), vertices.asFloatBuffer(), uniqueVertexCount, 3 * Float.BYTES, 1.05f);

            if(MeshOptimizer.meshopt_optimizeVertexFetchRemap(remap, indices.asIntBuffer()) != uniqueVertexCount) {
                throw new RuntimeException("Failed to remap");
            }
            remap(vertices, indices, remap, vertexFormat.getStride());

            MemoryUtil.memFree(remap);
            //streams.close();

            printStats(vertices, indices, (int) uniqueVertexCount);

            return uniqueVertexCount;
        }

        private static void remap(ByteBuffer vertexBuffer, ByteBuffer indexBuffer, IntBuffer remap, int vertex_size) {
            MeshOptimizer.meshopt_remapIndexBuffer(indexBuffer.asIntBuffer(), indexBuffer.asIntBuffer(), indexBuffer.remaining()/4, remap);
            MeshOptimizer.meshopt_remapVertexBuffer(vertexBuffer, vertexBuffer, remap.remaining(), vertex_size, remap);
        }

        private static void printStats(ByteBuffer vertices, ByteBuffer indices, int vertexCount) {
            try (MemoryStack stack = stackPush()) {
                MeshoptVertexCacheStatistics vcache = meshopt_analyzeVertexCache(
                        indices.asIntBuffer(),
                        vertexCount,
                        //32, 32, 32, // NVIDIA
                        14, 64, 128, // AMD
                        //128, 0, 0, // INTEL
                        MeshoptVertexCacheStatistics.malloc(stack)
                );
                System.out.println("ACMR: " + vcache.acmr());
                System.out.println("ATVR: " + vcache.atvr());

               /* MeshoptOverdrawStatistics overdraw = meshopt_analyzeOverdraw(
                        mesh.triangles(mesh.ntriangles() * 3),
                        mesh.points(mesh.npoints() * 3),
                        mesh.npoints(),
                        3 * Float.BYTES,
                        MeshoptOverdrawStatistics.malloc(stack)
                );
                System.out.println("Overdraw: " + overdraw.overdraw());
                System.out.println("Pixels covered: " + overdraw.pixels_covered());
                System.out.println("Pixels shaded: " + overdraw.pixels_shaded());

                */
            }
        }

        @Override
        public String getResourceName() {
            return "mesh_optimizer";
        }

        @Override
        public String getResourceOwner() {
            return "ourcraft";
        }
    };

}
