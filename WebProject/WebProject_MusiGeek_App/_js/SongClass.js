class Song{
    constructor(_parent/*, _floatBox */, _name, _lyrics, _audio){
        this.parent = _parent;
        // this.floatBox = _floatBox;
        this.name = _name;
        this.lyrics = _lyrics;
        this.audio = _audio;
    }

    addToHtml(){
        let newSong = document.createElement("h4");
        newSong.className = "col-md-4 col-12 p-3";
        newSong.innerHTML = this.name;
        this.parent.appendChild(newSong);

        console.log(this.parent.children);
        this.parent.onclick = function(){
            alert("parent");
        }.bind(this);
    }
}