class Musical {
    constructor(_parent, _extansion, _name, _img) {
        this.parent = _parent;
        this.extansion = _extansion;
        this.name = _name;
        this.img = _img;
    }

    addToHtml() {
        let newBox = document.createElement("div");
        newBox.className = "box col-md-5 col-12 border border-dark p-3 mb-5 overflow-hidden";
        this.parent.appendChild(newBox);

        let musicalDiv = document.createElement("div");
        musicalDiv.className = "col-8 overflow-hidden float-right";
        newBox.appendChild(musicalDiv);
        
        musicalDiv.innerHTML = `<img class="float-right ml-4" src="${this.img}" alt=${this.name}" poster">`;
        musicalDiv.innerHTML += `<h3 class="text-right">${this.name}</h3><br>`;

        
        let likeDiv = document.createElement("div");
        likeDiv.style.minHeight = "100%";
        likeDiv.className = "col-4 float-left";
        newBox.appendChild(likeDiv);

        let fav = document.createElement("i");
        fav.className = "fa fa-heart fa-5x";
        fav.style.color = "rgb(189, 189, 189)";
        
        likeDiv.appendChild(fav);

        if (localStorage[`${this.name}`]) {
            fav.style.color = "red";
        }

        musicalDiv.onclick = function(){
            window.open(`#${this.extansion.id}`,"_self");

            // Reset extantion elements.
            this.extansion.innerHTML = "";

            // Open the extansion with the musical's movie and songs.
            // Search for the right musical property in the main json.
            
            // Add new Movie.
            // Add list of Songs.
        }.bind(this);
        
        likeDiv.onclick = function () {
            // Save / unsave to favorites.
            if (localStorage[`${this.name}`]) {
                fav.style.color = "rgb(189, 189, 189)";
                localStorage.removeItem(this.name);
            }else{
                localStorage.setItem(this.name,true);
                fav.style.color = "red";
            }
        }.bind(this);
    }
}