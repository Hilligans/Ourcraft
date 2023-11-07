package dev.hilligans.ourcraft.data.other;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ObjFile {

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textures = new ArrayList<>();
    public ArrayList<Vector2f> normals = new ArrayList<>();

    public ArrayList<Vertex> fullVertices = new ArrayList<>();


    public ObjFile(String data) throws Exception {
        this(data,1,1);
    }

    public ObjFile(String data, int startSize, int endSize) throws Exception {
        try {
            for (String string : data.split("\n")) {
                string = string.trim();
                if (string.startsWith("vt ")) {
                    textures.add(remapTexture(getVector2f(string.substring(3).split(" ")),startSize,endSize));
                } else if (string.startsWith("vn ")) {
                    normals.add(getVector2f(string.substring(3).split(" ")));
                } else if (string.startsWith("v ")) {
                    vertices.add(getVector3f(string.substring(2).split(" ")));
                } else if (string.startsWith("f ")) {
                    fullVertices.add(new Vertex(string.substring(2)));
                }
            }
        } catch (Exception e) {
            throw new Exception("Failed to load file",e);
        }
    }


    public Vector2f getVector2f(String[] floats) throws Exception {
        return new Vector2f(Float.parseFloat(floats[0]),Float.parseFloat(floats[1]));
    }

    public Vector3f getVector3f(String[] floats) throws Exception {
        return new Vector3f(Float.parseFloat(floats[0]),Float.parseFloat(floats[1]),Float.parseFloat(floats[2]));
    }

    public Vector2f remapTexture(Vector2f vector2f, int ogSize, int newSize) {
        return new Vector2f(vector2f.x * ogSize / newSize,vector2f.y * ogSize / newSize);
    }

    public JSONObject toBlockModel() {
        JSONObject jsonObject = new JSONObject();
        JSONObject one = new JSONObject();
        jsonObject.put("1",one);

        JSONArray indices = new JSONArray();
        JSONArray vertices = new JSONArray();
        one.put("vertices",vertices);
        one.put("indices",indices);

        System.out.println(fullVertices.size() * 3);

        int x = 0;
        for(Vertex vertex : fullVertices) {
            if(vertex.size == 3) {
                indices.put(x);
                indices.put(x + 1);
                indices.put(x + 2);

                for (Vector3i vector3i : vertex.vertices) {
                    JSONArray vertexArray = new JSONArray();
                    //System.out.println(vector3i.x);
                    vertexArray.put(this.vertices.get(vector3i.x).x);
                    vertexArray.put(this.vertices.get(vector3i.x).y);
                    vertexArray.put(this.vertices.get(vector3i.x).z);

                    vertexArray.put(this.textures.get(vector3i.y).x);
                    vertexArray.put(this.textures.get(vector3i.y).y);
                    vertices.put(vertexArray);
                }
                x+=3;
            } else if(vertex.size == 4) {

                indices.put(x);
                indices.put(x + 1);
                indices.put(x + 2);
                indices.put(x);
                indices.put(x + 2);
                indices.put(x + 3);

                for (Vector3i vector3i : vertex.vertices) {
                    JSONArray vertexArray = new JSONArray();
                    vertexArray.put(this.vertices.get(vector3i.x).x);
                    vertexArray.put(this.vertices.get(vector3i.x).y);
                    vertexArray.put(this.vertices.get(vector3i.x).z);

                    vertexArray.put(this.textures.get(vector3i.y).x);
                    vertexArray.put(this.textures.get(vector3i.y).y);
                    vertices.put(vertexArray);
                }
                x+=6;


            }
        }




        return jsonObject;
    }

    public static class Vertex {

        public Vector3i[] vertices;
        public int size = 0;

        public Vertex(String line) {
            int x = 0;
            size = line.trim().split(" ").length;
            vertices = new Vector3i[size];
            for (String string : line.trim().split(" ")) {
                String[] vals = string.split("/");
                try {
                    vertices[x] = new Vector3i(Integer.parseInt(vals[0]) - 1, Integer.parseInt(vals[1]) - 1, Integer.parseInt(vals[2]) - 1);
                } catch (Exception e) {
                    vertices[x] = new Vector3i();
                }
                x++;
            }
        }


        @Override
        public String toString() {
            return "Vertex{" +
                    "vertices=" + Arrays.toString(vertices) +
                    '}';
        }
    }


}
