class Movie{
    constructor(_parent, _floatBox, _name, _prodaction, _img, _link){
        this.parent = _parent;
        this.name = _name;
        this.prodaction = _prodaction;
        this.img = _img;
        this.link = _link;
        this.floatBox = _floatBox;
    }

    addToHtml(){
        let newBox = document.createElement("div");
        newBox.className = "box col-md-5 col-12 border p-3 mt-md-5";
        newBox.style.minHeight = "200px";
        this.parent.appendChild(newBox);
        this.box = newBox;

        newBox.innerHTML = `<img class="float-right ml-4" src="${this.img}" alt="${this.name} pic">`;
        newBox.innerHTML += `<h2 class="float-right">${this.name}</h2><br><br>`;
        newBox.innerHTML += `<h4 class="float-right">${this.prodaction}</h4>`;

        // What happend when we click on movie.
        newBox.onclick = function(){
            // Open floating scrren with the movie
            let embed = this.link.replace("watch?v=","embed/");
            
            this.floatBox.box.style.display = "flex";
            this.floatBox.h2.innerHTML = this.name;
            this.floatBox.box.style.textAlign = "center";
            this.floatBox.iframe.innerHTML = `<iframe class="id_float_iframe" width="95%" height="450" src="${embed}" frameborder="0" allow="accelerometer; autoplay;encrypted-media; gyroscope;picture-in-picture"></iframe>`;

            // If there is no src- notify the user.
            if(embed == ""){
                this.floatBox.iframe.innerHTML = `<br><br><h3 class="text-warning">הסרט עדיין לא הגיע לאתר!</h3>`;
            }
        }.bind(this);
    }
}