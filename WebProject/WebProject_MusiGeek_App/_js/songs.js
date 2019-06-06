window.onload = function () {
    // Create all song collections.
    doAjax("_json/musicals.json", function (_musicals_ar) {
        for (let item of _musicals_ar) {
            let {
                name,
                songs
            } = item;
            createSongCollection(name, songs);
        }
    })
}

function createSongCollection(_name, _songs) {
    // Create collection div.
    let newDiv = document.createElement("div");
    newDiv.className = "border border-dark m-3";
    id_parent.appendChild(newDiv);

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

                    let newSong = new Song(newRow, name, lyrics, audio);
                    newSong.addToHtml();
                }
                //newRow.innerHTML += `<h4 class="col-md-4 col-12"></h4>`;
                let emptySong = new Song(newRow, "", "", "");
                emptySong.addToHtml();
            });
        }
    }

    xmlHttp.open("GET", _songs, true);
    xmlHttp.send();
}