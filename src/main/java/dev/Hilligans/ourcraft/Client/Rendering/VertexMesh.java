package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;

public class VertexMesh {

    /**
     * one of these have to be not null
     */
    public VertexFormat vertexFormat;
    public String vertexFormatName;

    public VertexMesh(String vertexFormatName) {
        this.vertexFormatName = vertexFormatName;
    }

    public VertexMesh(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
    }

}

