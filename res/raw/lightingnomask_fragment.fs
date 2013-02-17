uniform sampler2D u_TextureSampler0;

precision highp float;
varying       vec2 g_vTexture0;
uniform highp vec4 u_DiffuseColor;
varying highp vec3 DiffuseLight;
varying  vec3  SpecularLight;
varying  vec4  rimLight; // Not needed here but the shader vertex shader has this
varying highp vec4 vColors;
varying  highp float   alphaSpec;
varying  highp float   SurfaceAlpha;

void main() {
    highp vec4 texColor  = texture2D(u_TextureSampler0, g_vTexture0) ;
    texColor.rgb += (texColor.rgb * (1.0 - texColor.a));
    highp vec4 specular = vec4 (SpecularLight ,0.0);
    highp vec4 diffuse = vec4(DiffuseLight,1.0);
    gl_FragColor = (texColor * diffuse + specular)  * vColors;
    gl_FragColor.a *= SurfaceAlpha;
    
 
}
