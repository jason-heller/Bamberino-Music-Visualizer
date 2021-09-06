#version 150

in vec3 position;
in vec2 textureCoords;
in vec3 normals;
in vec3 colors;

out vec2 _textureCoords;
out vec3 _colors;
out vec3 _normals;

uniform mat4 viewProj;
uniform mat4 modelView;

void main(void){

	vec4 worldPos = modelView * vec4(position, 1.0);
	
	gl_Position = viewProj * worldPos;
	
	_textureCoords = textureCoords;
	_colors = colors;
	_normals = mat3(modelView) * normals;
}
