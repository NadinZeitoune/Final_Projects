class Musical {
    constructor(_parent, _extansion, _name, _img) {
        this.parent = _parent;
        this.extansion = _extansion;
        this.name = _name;
        this.img = _img;
    }

    addToHtml() {
        let newBox = document.createElement("div");
        newBox.className = "box col-md-5 col-12 border p-3 mt-md-5 ";
        this.parent.appendChild(newBox);

        newBox.innerHTML = `<img class="float-right ml-4" src="${this.img}" alt="poster">`;
        newBox.innerHTML += `<h3 class="float-right">${this.name}</h3><br>`;

        let fav = document.createElement("i");
        fav.className = "fa fa-heart fa-5x float-left border col-3";
        fav.style.color = "rgb(189, 189, 189)";
        newBox.appendChild(fav);

        if (localStorage[`${this.name}`]) {
            fav.style.color = "red";
        }

        newBox.onclick = function(){
            newBox.style.backgroundColor = "red";
        }.bind(this);
        
        fav.onclick = function () {
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