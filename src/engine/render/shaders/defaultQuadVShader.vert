 #version 150 core

uniform float rotation;
uniform vec2 scale;
uniform vec2 translation;
uniform vec2 resolution;
uniform vec2 resScale;
uniform vec2 cameraOffset;

in vec4 in_Position;
in vec4 in_Color;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {

        float u_angle = radians(rotation);
        float sin_factor = sin(u_angle);
        float cos_factor = cos(u_angle);

        vec4 pos = in_Position;
        vec2 halfRes = resolution;
        halfRes.x = resolution.x / 2.0;
        halfRes.y = resolution.y / 2.0;
        vec2 camCenter = cameraOffset - halfRes;
        camCenter.x = -camCenter.x;

        vec2 trans = translation;
        trans.y = - trans.y;
        pos.xy = ((in_Position.xy * mat2(cos_factor, sin_factor, -sin_factor, cos_factor)) + trans - camCenter ) / halfRes;
	gl_Position = pos;

	pass_Color = in_Color;
	pass_TextureCoord = in_TextureCoord;
}