package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.ourcraft.util.interfaces.TriConsumer;
import org.joml.Vector3d;

public class CurvedRayEngine {

    public TriConsumer<Double,Integer, BlockPos> method;
    public int size;
    public int topEndSize;
    public int bottomEndSize;
    public CurvedRay ray;
    public float rotation;

    public CurvedRayEngine(TriConsumer<Double,Integer, BlockPos> method, int size, int topEndSize, int bottomEndSize, CurvedRay ray, float rotation) {
        this.method = method;
        this.size = size;
        this.topEndSize = topEndSize;
        this.bottomEndSize = bottomEndSize;
        this.ray = ray;
        this.rotation = rotation;
    }

    public void run() {
        Vector3d vector3d = new Vector3d(Math.cos(Math.toRadians(rotation)),1, Math.sin(Math.toRadians(rotation)));

        float start;
        for(start = 0;start < size; start += 0.001f) {
            if(ray.getY1(start) < ray.getY2(start)) {
                start -= 0.05;
                break;
            }
        }
        double lastX = start;


        for(float x = start; x < size; x++) {
            double topY = ray.getY1(x);
            double lastTopY = ray.getY1(x + 1);
            int dif = (int) Math.ceil(Math.abs(lastTopY - topY));
            float val =  (Math.min(Math.max(dif,1),size) * 1.5f);
            for(int z = 0; z < val; z++) {
                topY = ray.getY1(x + 1f / val * z);
                double angle = Math.atan((lastX / (x)) / (lastTopY / topY));
                method.accept(angle, 1, new BlockPos(x, topY, 0).multiply(vector3d));
            }

            topY = ray.getY2(x);
            lastTopY = ray.getY2(x + 1);
            dif = (int) Math.ceil(Math.abs(lastTopY - topY));
            val =  (Math.min(Math.max(dif,1),size) * 1.5f);
            for(int z = 0; z < val; z++) {
                topY = ray.getY2(x + 1f / val * z);
                double angle = Math.atan((lastX / (x)) / (lastTopY / topY));
                method.accept(angle, 1, new BlockPos(x, topY, 0).multiply(vector3d));
            }

            lastX = x;
        }
    }




}
