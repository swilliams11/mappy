// Passing a named function instead of an anonymous function.
function getWindowScreenSize( ) {
    // Code to run when the document is ready.
	var width = $(window).width();
	var height = $(window).height();
	console.log(width + " " + height);
}
 

// Shorthand for $( document ).ready()
/*
$(function() {
    console.log( "ready!" );
});
*/
// A $( document ).ready() block.
$( document ).ready( getWindowScreenSize);

//Returns width of browser viewport
/*
$( window ).width();
$( window ).height();
 */

// Returns width of HTML document
//$( document ).width();