<!--
 * --------------------------------------------------------------------------------------------------------------
 * -	Brunel University London & Dublin City University														-
 * -	H2020 NEWTON Project		       																		-
 * -	A Mulseplayer that uses Exhalia, RIVAL 700 haptic mouse, and Silent Shark Fan wind device				-
 * -	for rendering mulsemedia effects on the Web										       					-
 * -	2018																									-
 * -	Version 3.0																								-
 * --------------------------------------------------------------------------------------------------------------
 */
 -->
<html>
<head>
<link rel="stylesheet" type="text/css" href="./css/mulseplayer.css"/>
<meta charset="utf-8">
<title>Mulseplayer version 3.0</title>
</head>
<body>

<%@page import="java.io.*" %> 
<%@page import="java.util.*" %> 
<%! public void GetDirectory(String a_Path, Vector a_files, Vector a_folders) {
		File l_Directory = new File(a_Path);
		File[] l_files = l_Directory.listFiles();

		for (int c = 0; c < l_files.length; c++) {
			if (l_files[c].isDirectory()) {
				a_folders.add(l_files[c].getName());
			} else {
				a_files.add(l_files[c].getName());
			}
		}
	}
%>
<div class="content">
<div>
	<a><img class="image" src="img/BRUNELlogo.jpg"/></a>
	<a><img class="image" src="img/newton.png"/></a>
	<a><img class="image" src="img/euro.jpg"/></a>
</div>

<div id="myid"></div>

<form>
	<%
		Vector l_Files = new Vector(), l_Folders = new Vector();
		String current = new java.io.File( "." ).getCanonicalPath();
		GetDirectory(current+"/webapps/mulseplayer/videos", l_Files, l_Folders); 

		out.println("<select id='videoFile'>");
		out.println("<option value=''>Select a media file</option>");
		for (int a = 0; a < l_Files.size(); a++) {
			String s = l_Files.elementAt(a).toString();
			out.println("<option value='" + s + "'>" + s + "</option>");
		}
		out.println("</select>");
	%>
	Learner preferences:  
	<input type="checkbox" id="olfaction"> Olfaction</input>
	<input type="checkbox" id="haptic"> Haptic</input>	
	<input type="checkbox" id="wind"> Wind</input>	
</form>
<br>
<video id="v0" controls onpause="reloadPage()" tabindex="0" autobuffer preload>
	<source type="video/mp4; codecs=&quot;avc1.42E01E, mp4a.40.2&quot;" id="src_id"/> 
    <p>Sorry, your browser does not support the &lt;video&gt; element.</p>
</video>
<p id="time"></p>
</div>
<script src="./js/jquery-2.1.1.js"></script>
<script src="./js/mulseplayer.js"></script>

</body>
</html>
