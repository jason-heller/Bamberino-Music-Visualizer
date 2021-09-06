#version 150

in vec2 _textureCoords;
in vec3 _normals;
in vec3 _colors;

uniform vec3 lightVec;
uniform sampler2D albedoTexture;

//const float levels = 3.0;

out vec4 finalColor;

void main(void){
	vec4 albedo = texture(albedoTexture, _textureCoords.xy);
	vec4 color = vec4(_colors, 1.0);

	float brightness = 0.8 + (0.2 * dot(-lightVec, normalize(_normals)));
	//float level = floor(brightness * levels);
	//float lightFactor = (level / levels);

	finalColor = albedo + color * brightness;
}
