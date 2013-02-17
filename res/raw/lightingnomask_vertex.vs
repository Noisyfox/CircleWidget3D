attribute vec3 a_Position;
attribute vec3 a_Normals;
attribute vec4 a_Colors;
attribute vec2 a_TexCoords;
uniform  mat4 u_MVPMatrix;
uniform  mat4 u_MVMatrix;
uniform  mat3 u_MVITMatrix;
uniform  vec3 u_AmbientColor;
uniform  vec4 u_DiffuseColor;
uniform  vec3 u_SpecularColor;
uniform  float u_Shininess;
uniform  vec3 u_LightColor0;
uniform  vec4 u_LightPosition0;
uniform  vec4 u_LightAmbientColor0;
uniform  vec3 u_LightColor1;
uniform  vec4 u_LightPosition1;
uniform  vec4 u_LightAmbientColor1;
vec4  vPositionES;
vec4  vLightPos;
vec3  vNormalES;
varying  vec2  g_vTexture0; 
varying  highp float   alphaSpec; 
varying  vec4  vColors;
varying  vec3  DiffuseLight0;
varying  vec3  SpecularLight0;
varying  vec3  DiffuseLight1;
varying  vec3  SpecularLight1;
varying  highp float   SurfaceAlpha;

void Lighting0(highp vec3 normal, highp vec3 eyeDir, highp vec3 lightDir) {
    float NdotL = max(dot(normal, lightDir), 0.0);

    DiffuseLight0 += (NdotL*u_LightColor0*vec3(u_DiffuseColor.rgb));
    DiffuseLight0 += u_AmbientColor;

    if (NdotL > 0.0) {
        vec3 halfVector = normalize(lightDir + eyeDir);
        float NdotH = max(dot(normal, halfVector), 0.0);
        float specular = pow(NdotH, u_Shininess);
        SpecularLight0 += specular;       
        SpecularLight0 *= u_SpecularColor;
    }
}

void Lighting1(highp vec3 normal, highp vec3 eyeDir, highp vec3 lightDir) {
    float NdotL = max(dot(normal, lightDir), 0.0);
    DiffuseLight1 += (NdotL*u_LightColor1*vec3(u_DiffuseColor.rgb)) ;
    DiffuseLight1 += u_AmbientColor;

    if (NdotL > 0.0) {
        vec3 halfVector = normalize(lightDir + eyeDir);
        float NdotH = max(dot(normal, halfVector), 0.0);
        float specular = pow(NdotH, u_Shininess);
        SpecularLight1 += specular;      
        SpecularLight1 *= u_SpecularColor;
    }
}

void DirectionalLight0(highp vec3 normal, highp vec3 vertexPos) {
    vec3 lightDir = normalize( vLightPos.xyz - vertexPos );
    vec3 eyeDir = -normalize(vertexPos);
    Lighting0(normal, eyeDir, lightDir);
}


void DirectionalLight1(highp vec3 normal, highp vec3 vertexPos) {
    vec3 lightDir = normalize( vLightPos.xyz - vertexPos );
    vec3 eyeDir = -normalize(vertexPos);
    Lighting1(normal, eyeDir, lightDir);
}

void main() {
    gl_Position = u_MVPMatrix * vec4(a_Position,1.0);
    vPositionES = u_MVMatrix * vec4(a_Position,1.0);
    vLightPos = u_LightPosition0;
    vNormalES = u_MVITMatrix *  a_Normals;
    g_vTexture0 = a_TexCoords;
    vColors = a_Colors;
    DiffuseLight0 = vec3(0.0);
    SpecularLight0 = vec3(0.0);
    DiffuseLight1 = vec3(0.0);
    SpecularLight1 = vec3(0.0);
    SurfaceAlpha = u_DiffuseColor.a;
    DirectionalLight0(normalize(vNormalES), vPositionES.xyz);    
    vLightPos = u_LightPosition1;
    DirectionalLight1(normalize(vNormalES), vPositionES.xyz);
    alphaSpec = clamp(SpecularLight0.r + SpecularLight1.r,0.0,1.0);
}
