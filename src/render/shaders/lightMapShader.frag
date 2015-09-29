uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform vec3 objectColor;
uniform float screenHeight;
uniform float screenWidth;
uniform sampler2D backbuffer;
uniform float lightRadius;
uniform float scale;
uniform float power;
void main() {
	vec2 position;
        position.x = gl_FragCoord.x/1920;
        position.y = gl_FragCoord.y/1080;
        vec4 texcolor = texture2D(backbuffer, position);
        float distance = length(lightLocation - gl_FragCoord.xy);
        distance/=scale;
        float attenuation;

        if(distance<50)
            attenuation = 1;
        else if(distance<100)
            attenuation = power*1-(distance-50)/300;
        else{
            attenuation = power*1-50/300.0-(distance-100)/200;
        }
 
	vec4 color = vec4(texcolor.x+attenuation, texcolor.y+attenuation, texcolor.z+attenuation,1.0f);

	gl_FragColor = color;
}