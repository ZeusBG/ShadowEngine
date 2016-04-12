uniform sampler2D image;
uniform vec2 dir;
uniform vec2 resolution;
float offset[5] = { 0, 1, 2 , 3 , 4 };
float weight[5] = { 0.16 , 0.15 , 0.12 , 0.09 , 0.05 };
float pivot = 100;




void main(void)
{
    int i;
    
    vec2 position;
    position.x = gl_FragCoord.x/resolution.x;
    position.y = 1-gl_FragCoord.y/resolution.y;
    vec4 color;
    color = texture2D( image, position ) * weight[0];
    for (i=1; i<5; i++) {
        color +=texture2D( image, (vec2(gl_FragCoord.x,resolution.y-gl_FragCoord.y)+dir*offset[i] )/resolution ) * weight[i];
        color +=texture2D( image, (vec2(gl_FragCoord.x,resolution.y-gl_FragCoord.y)-dir*offset[i] )/resolution ) * weight[i];
    }


    gl_FragColor = color;


}