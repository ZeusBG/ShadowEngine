uniform sampler2D image;
uniform vec2 dir;
uniform vec2 playerView;
float offset[5] = { 0, 1, 2 , 3 , 4 };
float weight[5] = { 0.16 , 0.15 , 0.12 , 0.09 , 0.05 };
float pivot = 100;




void main(void)
{
    int i;
    float distance = length(playerView-gl_FragCoord);
    
    vec2 resolution = vec2(1920,1080);
    vec2 position;
    position.x = gl_FragCoord.x/1920;
    position.y = 1-gl_FragCoord.y/1080;
    vec4 color;
    color = texture2D( image, position ) * weight[0];
    for (i=1; i<5; i++) {
        color +=texture2D( image, (vec2(gl_FragCoord.x,1080-gl_FragCoord.y)+dir*offset[i] )/resolution ) * weight[i];
        color +=texture2D( image, (vec2(gl_FragCoord.x,1080-gl_FragCoord.y)-dir*offset[i] )/resolution ) * weight[i];
    }
    gl_FragColor = color;


}