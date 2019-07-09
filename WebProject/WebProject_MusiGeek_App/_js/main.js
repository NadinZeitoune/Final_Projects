window.onload = function(){
    
}

// Responsable for the menu burger click.
function showMenu(){
    let index = id_menu.className.indexOf(" d-block");
    console.log(index);
    if(index < 0){
        id_menu.className += " d-block";
        console.log(id_menu.className);
    }else{
        id_menu.className = id_menu.className.replace(" d-block", "");
        console.log(id_menu.className);
    }
}

function doAjax(_link, _func){
    let xmlHttp = new XMLHttpRequest();
    
    xmlHttp.onreadystatechange = function(){
        if(this.status == 200 && this.readyState == 4){
            var json_ar = JSON.parse(this.response);
            _func(json_ar);
        }
    }

    xmlHttp.open("GET", _link, true);
    xmlHttp.send();
}

function goA(_aLink){
    if(_aLink == ""){return}
    window.open(_aLink + ".html", "_self");
}

// Songs functions.
function createSongCollection(_parent, _floatBox, _name, _songs) {
    // Create collection div.
    let newDiv = document.createElement("div");
    newDiv.className = "border border-dark m-3";
    _parent.appendChild(newDiv);

    newDiv.innerHTML = `<h2 class="text-center">${_name}</h2>`;

    let newRow = document.createElement("div");
    newRow.className = "row justify-content-between text-center p-3";
    newDiv.appendChild(newRow);


    let xmlHttp = new XMLHttpRequest();

    xmlHttp.onreadystatechange = function () {
        if (this.status == 200 && this.readyState == 4) {
            var json_ar = JSON.parse(this.response);
            
            //Get the songs for the musical.
            doAjax(_songs, function (json_ar) {
                for (let item of json_ar) {
                    let {
                        name,
                        lyrics,
                        audio
                    } = item;

                    let newSong = new Song(newRow, _floatBox, name, lyrics, audio);
                    newSong.addToHtml();
                }
                
                let emptySong = new Song(newRow, "", "", "", "");
                emptySong.addToHtml();
            });
        }
    }

    xmlHttp.open("GET", _songs, true);
    xmlHttp.send();
}

function returnLyrics(_lyrics){
    while(_lyrics.indexOf("%") != -1){
        _lyrics = _lyrics.replace("%", "\n");
    }
    return _lyrics;
}