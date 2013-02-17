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
uniform  vec3 u_LightDirection0;
uniform  vec3 u_LightColor1;
uniform  vec3 u_LightDirection1;
uniform  vec3 u_LightDirection3;


vec4  vPositionES;
vec3  vLightDir;
vec3  vNormalES;
vec3 DiffuseLight0;
vec3 SpecularLight0;
vec3 DiffuseLight1;
vec3 SpecularLight1;    
highp vec2   shadowDir;
highp vec2 shadowUV;

varying  vec2  g_vTexture0; 
varying  vec2  g_vTexture1; 
varying  highp float   alphaSpec; 
varying  vec4  vColors;
varying  vec3  DiffuseLight;
varying  vec3  SpecularLight;
varying  vec4  rimLight;
varying  highp float   SurfaceAlpha;


highp float    offset;


void Lighting0(highp vec3 normal, highp vec3 eyeDir, highp vec3 lightDir) {
    float NdotL = max(dot(normal, lightDir), 0.0);

    DiffuseLight0 += (NdotL*u_LightColor0 * vec3(u_DiffuseColor.rgb));
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
    DiffuseLight1 += (NdotL*u_LightColor1 * vec3(u_DiffuseColor.rgb)) ;
    DiffuseLight1 += u_AmbientColor;

    if (NdotL > 0.0) {
        vec3 halfVector = normalize(lightDir + eyeDir);
        float NdotH = max(dot(normal, halfVector), 0.0);
        float specular = pow(NdotH, u_Shininess);
        SpecularLight1 += specular;      
        SpecularLight1 *= u_SpecularColor;
        
        
        
    }
}

void Lighting2(highp vec3 normal, highp vec3 eyeDir, highp vec3 lightDir) {
    float NdotL = max(dot(normal, lightDir), 0.0) ;
    rimLight = vec4(lightDir,0.0);
    

    if (NdotL > 0.0) {
        vec3 halfVector = normalize(lightDir + eyeDir);
        float NdotH = max(dot(normal, halfVector), 0.0);
        float specular = pow(NdotH, 32.0);
        rimLight.a = specular * 2.0 ;                      
    }
}

void DirectionalLight0(highp vec3 normal, highp vec3 vertexPos) {
    vec3 lightDir =  vLightDir;
    vec3 eyeDir = -normalize(vertexPos);
    Lighting0(normal, eyeDir, lightDir);
}


void DirectionalLight1(highp vec3 normal, highp vec3 vertexPos) {
    vec3 lightDir = vLightDir;
    vec3 eyeDir = -normalize(vertexPos);
    Lighting1(normal, eyeDir, lightDir);
}


void DirectionalLight2(highp vec3 normal, highp vec3 vertexPos) {
    vec3 lightDir = vLightDir ;
    vec3 eyeDir = -normalize(vertexPos);
    Lighting2(normal, eyeDir, lightDir);
}

void main() {
    gl_Position = u_MVPMatrix * vec4(a_Position,1.0);
    vPositionES = u_MVMatrix * vec4(a_Position,1.0);
    vNormalES = u_MVITMatrix *  a_Normals;
    
    g_vTexture0 = a_TexCoords;
    
    vColors = a_Colors;
    DiffuseLight0 = vec3(0.0);
	SpecularLight0 = vec3(0.0);
	DiffuseLight1 = vec3(0.0);
	SpecularLight1 = vec3(0.0);    
    DiffuseLight = vec3(0.0);
    SpecularLight = vec3(0.0);
    rimLight = vec4(0.0);
    offset =1.5 *   0.00401606;

    

    SurfaceAlpha = u_DiffuseColor.a;
    shadowDir = -vNormalES.xy;
    vLightDir = u_LightDirection0;
    DirectionalLight0(normalize(vNormalES), vPositionES.xyz);    
    vLightDir = u_LightDirection1;
    DirectionalLight1(normalize(vNormalES), vPositionES.xyz);
    vLightDir = u_LightDirection3;
    DirectionalLight2(normalize(vNormalES), vPositionES.xyz);    
    alphaSpec = clamp(SpecularLight0.r + SpecularLight1.r,0.0,1.0);
    SpecularLight =SpecularLight0 + SpecularLight1 ;
    DiffuseLight = DiffuseLight0 +  DiffuseLight1 ;
    shadowUV = vec2(offset  * shadowDir.x, offset * shadowDir.y ) ;
    g_vTexture1 = a_TexCoords + shadowUV;
}
