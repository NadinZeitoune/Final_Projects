class Musical {
    constructor(_parent, _extansion, _name, _img) {
        this.parent = _parent;
        this.extansion = _extansion;
        this.name = _name;
        this.img = _img;
    }

    addToHtml() {
        let newBox = document.createElement("div");
        newBox.className = "box col-md-5 col-12 border border-info p-3 mt-5 overflow-hidden";
        this.parent.appendChild(newBox);

        let div1 = document.createElement("div");
        div1.className = "border border-warning col-8 overflow-hidden float-right";
        newBox.appendChild(div1);
        
        div1.innerHTML = `<img class="float-right ml-4" src="${this.img}" alt=${this.name}" poster">`;
        div1.innerHTML += `<h3 class="text-right">${this.name}</h3><br>`;

        
        let div2 = document.createElement("div");
        div2.className = "border border-danger col-4 float-left";
        newBox.appendChild(div2);

        let fav = document.createElement("i");
        fav.className = "fa fa-heart fa-5x";
        fav.style.color = "rgb(189, 189, 189)";
        div2.appendChild(fav);

        if (localStorage[`${this.name}`]) {
            fav.style.color = "red";
        }

        div1.onclick = function(){
            div1.style.backgroundColor = "red";
        }.bind(this);
        
        div2.onclick = function () {
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