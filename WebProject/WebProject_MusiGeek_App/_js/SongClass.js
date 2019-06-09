class Song {
    constructor(_parent , _floatBox, _name, _lyrics, _audio) {
        this.parent = _parent;
        this.floatBox = _floatBox;
        this.name = _name;
        this.lyrics = _lyrics;
        this.audio = _audio;
    }

    addToHtml() {
        var newSong = document.createElement("div");
        newSong.className = "col-md-4 col-12 p-3";
        newSong.innerHTML = this.name;
        this.parent.appendChild(newSong);

        newSong.onclick = function(){
            if(this.name != ""){
                // Open floating box with the lyrics.
                this.floatBox.box.style.display = "flex";
                this.floatBox.box.style.textAlign = "center";
                this.floatBox.h2.innerHTML = this.name;
                
                // Check if there is src for the audio before!
                this.floatBox.audio.innerHTML += `<source src="${this.audio}" type="audio/mpeg">`;
                
                // for the lyrics. replace % with ENTER. <pre/>
                this.floatBox.div.innerHTML = `<pre>${returnLyrics(this.lyrics)}</pre>`;
            }
        }.bind(this);
        
    }
}