class Song {
    constructor(_parent /*, _floatBox */ , _name, _lyrics, _audio) {
        this.parent = _parent;
        // this.floatBox = _floatBox;
        this.name = _name;
        this.lyrics = _lyrics;
        this.audio = _audio;
    }

    addToHtml() {
        var newSong = document.createElement("div");
        // newSong.className = "col-md-4 col-12 p-3";
        newSong.innerHTML = this.name + "lplp";
        newSong.style.backgroundColor = "yellow";
        newSong.style.cursor = "pointer";

        //console.log(newSong)
        newSong.addEventListener("click", function () {
            alert("aaaa");
        })
        this.parent.appendChild(newSong);
    }
}