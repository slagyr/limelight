
function load() 
{
	var world = document.getElementById('world');
	for(var row = 0; row < 99; row++)
	{
		var r = document.createElement('tr');
		r.setAttribute('class', 'row');
		world.appendChild(r);
		for(var col = 0; col < 99; col++)
		{
			var c = document.createElement('td');
			c.setAttribute('class', 'cell');
			c.setAttribute('id', '' + row + "," + col);
			r.appendChild(c);	
		}
	}
}

function start()
{
	
}