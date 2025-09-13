package dev.hilligans.engine.client.graphics.vulkan.boilerplate.window;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.shaderc.Shaderc;

import java.nio.ByteBuffer;

import static org.lwjgl.util.shaderc.Shaderc.*;
import static org.lwjgl.vulkan.VK10.*;

public class ShaderCompiler {

    static long options;
    static long compiler;

    static {
       // Shaderc.shaderc_compile_options_set_target_env(options, Shaderc.shaderc_target_env_vulkan, Shaderc.shaderc_env_version_vulkan_1_0);
    }

    public static ByteBuffer compileShader(String shaderCode, String path, int shaderType) {
        ByteBuffer compiledShader;
        long compiler = Shaderc.shaderc_compiler_initialize();
        long options = Shaderc.shaderc_compile_options_initialize();
        Shaderc.shaderc_compile_options_set_forced_version_profile(options, 450, shaderc_profile_core);
        try {

            long result = Shaderc.shaderc_compile_into_spv(
                    compiler,
                    shaderCode,
                    vulkanStageToShadercKind(shaderType),
                    path,
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

    public static String preprocessShader(String shader) {
        long compiler = Shaderc.shaderc_compiler_initialize();
        long options = Shaderc.shaderc_compile_options_initialize();
        Shaderc.shaderc_compile_options_add_macro_definition(options, "OPENGL", "1");
        try {
            long result = shaderc_compile_into_preprocessed_text(compiler, shader, shaderc_vertex_shader, "", "main", options);

            long buf = nshaderc_result_get_bytes(result);
            String val = MemoryUtil.memUTF8(buf);
            shaderc_result_release(result);
            return val;
        } finally {
            Shaderc.shaderc_compile_options_release(options);
            Shaderc.shaderc_compiler_release(compiler);
        }
    }

    private static int vulkanStageToShadercKind(int stage) {
        return switch (stage) {
            case VK_SHADER_STAGE_VERTEX_BIT -> shaderc_vertex_shader;
            case VK_SHADER_STAGE_FRAGMENT_BIT -> shaderc_fragment_shader;
            case VK_SHADER_STAGE_COMPUTE_BIT -> shaderc_compute_shader;
            default -> throw new IllegalArgumentException("Stage: " + stage);
        };
    }
}
