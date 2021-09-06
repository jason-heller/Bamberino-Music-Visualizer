song.txt controls how the scene will work out, format is as such:

block {
	sometag = "whatever"
}

I provided some stuff in scene.txt to help you out.

Theres 2 kinds of blocks, song and stem.

Song looks like this:

song {
    title = "Song name goes here"			<-- name of the song
    subtitle = "Subtitle text goes here"	<-- if you wanna put text under the title
    title_x = "1280"						<-- X position of title/subtitle on screen (out of 1920) \
    title_y = "870"							<-- Y position of title/subtitle on screen (out of 1080) / 0,0 is top left most corner of screen
    start_time = "3000"						<-- how long (in milliseconds) to wait to start the song, also controls fadein/fadeout
    background_color = "#67bf43"			<-- background color in hex
}

The stem tab adds stems to the scene:

stem {
    file = "C:\Users\Jason\Desktop\whatever.wav"		<-- path to wav file, must be mono, signed 16 bit
    scene_reactor = "burner 2"							<-- what part of the scene the stem controls
}

scene_reactor can take in the following inputs:

	"cutlery"		<-- the spoons and stuff that spin
	"drawer"		<-- the drawer that opens/closes
	"cabinet 0"		<-- leftmost cabinet
	"cabinet 1"		<-- middle cabinet
	"cabinet 2"		<-- rightmost cabinet
	"tomato"		<-- the hopping tomato
	"burner 0"		<-- bottom-left most burner		[ ][ ]
													[x][ ]
													
	"burner 1"		<-- top-left most burner		[x][ ]
													[ ][ ]
													
	"burner 2"		<-- bottom-right most burner	[ ][ ] (this also controls the pan and the tofu)
													[ ][x]
													
	"burner 3"		<-- top-right most burner		[ ][x]
													[ ][ ]
		

Other important stuff:
	
For the title/subtitle you can change the color per-character! By default text is white.
Typing in '#_' minus the quotes will tell the program to change all subsequent characters 
to that color (where _ is a character defining the color to use)
That's a little confusing so heres an example:

title = "The #*middle of this sentence #wis rainbow"

If you set the title to this, #* sets the color to rainbow, and #w sets it back to white.

Here is a list of all possible colors (let me know if you want others!)
NOTE: These are case-sensitive

#r		<-- red
#o		<-- orange
#y		<-- yellow
#g		<-- green
#c		<-- cyan
#b		<-- blue
#p		<-- pink
#i		<-- indigo
#v		<-- violet
#w		<-- white
#s		<-- light silver
#S		<-- dark silver
#0		<-- black
#G		<-- gold
#R		<-- rainbow (each character is a diff hue, but the colors stay the same)
#*		<-- rainbow (all characters are the same color, but the colors cycles through the whole spectrum)
#M		<-- marquee (text has a marquee effect, they shift from black to white)
#A		<-- alert	(pulses red)
		
		