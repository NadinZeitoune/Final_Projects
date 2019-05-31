class Movie{
    constructor(_parent, _name, _prodaction, _img, _link){
        this.parent = _parent;
        this.name = _name;
        this.prodaction = _prodaction;
        this.img = _img;
        this.link = _link;
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
            this.box.innerHTML += `<iframe width="560" height="315" src="${embed}" alt="sorry" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" ></iframe>`;
        }.bind(this);
    }
}