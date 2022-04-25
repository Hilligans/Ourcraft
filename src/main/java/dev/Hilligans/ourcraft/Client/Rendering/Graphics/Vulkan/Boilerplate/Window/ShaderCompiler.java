package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Vulkan.Boilerplate.Window;

import org.lwjgl.util.shaderc.Shaderc;

import java.nio.ByteBuffer;

import static org.lwjgl.vulkan.VK10.*;

public class ShaderCompiler {

    public static ByteBuffer compileShader(String shaderCode, int shaderType) {
        long compiler = 0;
        long options = 0;
        ByteBuffer compiledShader;

        try {
            compiler = Shaderc.shaderc_compiler_initialize();
            options = Shaderc.shaderc_compile_options_initialize();

            long result = Shaderc.shaderc_compile_into_spv(
                    compiler,
                    shaderCode,
                    shaderType,
                    "shader.glsl",
                    "main",
                    options
            );

            if (Shaderc.shaderc_result_get_compilation_status(result) != Shaderc.shaderc_compilation_status_success) {
                throw new RuntimeException("Shader compilation failed: " + Shaderc.shaderc_result_get_error_message(result));
            }

            compiledShader = Shaderc.shaderc_result_get_bytes(result);
        } finally {
            Shaderc.shaderc_compile_options_release(options);
            Shaderc.shaderc_compiler_release(compiler);
        }

        return compiledShader;
    }

    public static final int VERTEX_SHADER = VK_SHADER_STAGE_VERTEX_BIT;
    public static final int FRAGMENT_SHADER = VK_SHADER_STAGE_FRAGMENT_BIT;
    public static final int GEOMETRY_SHADER = VK_SHADER_STAGE_GEOMETRY_BIT;
}
