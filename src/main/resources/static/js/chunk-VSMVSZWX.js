import{b as j}from"./chunk-UJYHWII2.js";import{A as g,Ba as T,Bb as y,C as c,Cb as U,F as O,G as P,H as M,I as r,J as o,K as _,L as I,M as A,N as k,O as p,P as v,U as a,V as E,Va as V,W as f,Wa as N,ma as C,na as F,o as x,qb as S,r as u,s as h,vb as w,x as s,y as m,za as b}from"./chunk-Q3UIBATL.js";function Y(n,l){if(n&1&&(r(0,"option",8),a(1),o()),n&2){let e=l.$implicit;c("value",e.id),s(),E(e.nomSemetre)}}function G(n,l){if(n&1&&(a(0),_(1,"br")),n&2){let e=l.$implicit;f(" ",e.nomModule," ")}}function H(n,l){if(n&1&&(a(0),_(1,"br")),n&2){let e=l.$implicit;f(" ",e.coefficient," ")}}function J(n,l){if(n&1&&(r(0,"tr")(1,"td"),a(2),o(),r(3,"td"),P(4,G,2,1,"br",null,O),o(),r(6,"td",6),P(7,H,2,1,"br",null,O),o()()),n&2){let e=l.$implicit;s(2),E(e.idUe.nomUE),s(2),M(e.modules),s(3),M(e.modules)}}var ee=(()=>{class n{constructor(e,t,i,d){this.icons=e,this.semestreService=t,this.classService=i,this.root=d,this.ues=[],this.semestres=[],this.classesArchive=[]}ngOnInit(){this.load_ues()}load_ues(){this.root.queryParams.subscribe(e=>{this.idClasseNivFil=e.id,this.semestreService.getCurrentSemestresByIdNivFiliere(this.idClasseNivFil).subscribe(t=>{t.forEach(i=>{this.semestres.some(d=>d.id==i.id)||this.semestres.push(i)}),console.log(this.semestres,"semestre")})})}onSelect(e){let t=e.target.value;this.classService.getAll_ue(this.idClasseNivFil,t).subscribe(i=>{this.ues=i,console.log(this.ues,"ues")})}goBack(){window.history.back()}static{this.\u0275fac=function(t){return new(t||n)(m(S),m(j),m(y),m(b))}}static{this.\u0275cmp=x({type:n,selectors:[["app-view-ue"]],decls:18,vars:3,consts:[[1,"back-button",3,"click"],[3,"icon"],[3,"change"],["selected","","disabled","","value",""],[3,"value",4,"ngFor","ngForOf"],[1,"table","table-bordered"],[1,"coef"],[4,"ngFor","ngForOf"],[3,"value"]],template:function(t,i){t&1&&(r(0,"main")(1,"div",0),p("click",function(){return i.goBack()}),_(2,"fa-icon",1),o(),r(3,"select",2),p("change",function(z){return i.onSelect(z)}),r(4,"option",3),a(5,"Semestre"),o(),g(6,Y,2,2,"option",4),o(),r(7,"table",5)(8,"thead")(9,"tr")(10,"th"),a(11,"Nom"),o(),r(12,"th"),a(13,"ECUE"),o(),r(14,"th",6),a(15,"Coefficient"),o()()(),r(16,"tbody"),g(17,J,9,1,"tr",7),o()()()),t&2&&(s(2),c("icon",i.icons.back),s(4),c("ngForOf",i.semestres),s(11),c("ngForOf",i.ues))},dependencies:[C,V,N,w],styles:[".back-button[_ngcontent-%COMP%]{width:30px;height:30px;display:flex;align-items:center;padding:8px;font-size:20px;margin:10px 0}.back-button[_ngcontent-%COMP%]:hover{background-color:var(--tertiary);color:var(--primary);border:none;border-radius:50%}select[_ngcontent-%COMP%]:focus{outline:none;box-shadow:none}select[_ngcontent-%COMP%]{border:none;margin-bottom:10px}.coef[_ngcontent-%COMP%]{text-align:center}"]})}}return n})();function K(n,l){if(n&1){let e=k();r(0,"div",16)(1,"li")(2,"a",13),p("click",function(){u(e);let i=v(3);return h(i.toggle_to_presence(i.classeSelect.id))}),a(3,"Liste des \xE9tudiants"),o()(),r(4,"li")(5,"a",13),p("click",function(){u(e);let i=v(3);return h(i.toggle_to_noteSemestre(i.classeSelect.id))}),a(6,"Notes du semestre"),o()(),r(7,"li")(8,"a",13),p("click",function(){u(e);let i=v(3);return h(i.toggle_to_view_ues(i.classeSelect))}),a(9,"Unit\xE9s d'enseignement"),o()()()}}function Q(n,l){if(n&1&&(I(0),g(1,K,10,0,"div",15),A()),n&2){let e=v(2);s(),c("ngIf",e.isShow_link_modal)}}function W(n,l){if(n&1){let e=k();r(0,"div",4)(1,"div",5)(2,"div",6)(3,"div",7),_(4,"fa-icon",8),o(),r(5,"div",9)(6,"p",10),a(7),o(),r(8,"p",11),a(9),o(),r(10,"div",12)(11,"a",13),p("click",function(){let i=u(e).$implicit,d=v();return h(d.show_views(i))}),a(12,"Clique ici"),_(13,"fa-icon",1),o()()()(),g(14,Q,2,1,"ng-container",14),o()()}if(n&2){let e=l.$implicit,t=v();s(4),c("icon",t.icons.boxarchive),s(3),f("Promotion ",e.idAnneeScolaire.ans,""),s(2),f("Effectifs : ",e.effectifs,""),s(4),c("icon",t.icons.angleDown),s(),c("ngIf",e===t.classeSelect)}}var ne=(()=>{class n{constructor(e,t,i,d){this.icons=e,this.router=t,this.classService=i,this.root=d,this.classesArchives=[],this.isShow_link_modal=!0}ngOnInit(){this.load_archives(),this.getPermission()}load_archives(){this.root.queryParams.subscribe(e=>{this.idNivFiliere=+e.id}),this.classService.getAllArchivesByClasseIdNivFil(this.idNivFiliere).subscribe(e=>{this.classesArchives=e,this.classesArchives.forEach(t=>{let i=new Date(t.idAnneeScolaire.finAnnee);t.idAnneeScolaire.ans=i.getFullYear()}),console.log(this.classesArchives,"archivesss")})}show_views(e){console.log(e,"dois etre changer"),this.classeSelect===e?this.classeSelect=null:this.classeSelect=e}toggle_to_presence(e){let t={queryParams:{id:e}};this.getPermission()?this.router.navigate(["/r-scolarite/etudiant-de-la-classe"],t):this.router.navigate(["/dga/etudiant-de-la-classe"],t)}toggle_to_noteSemestre(e){let t={queryParams:{id:e}};this.getPermission()?this.router.navigate(["/r-scolarite/all-notes"],t):this.router.navigate(["/dga/all-notes"],t)}toggle_to_view_ues(e){console.log(e,"----------------class");let t={queryParams:{id:e.id}};this.getPermission()?this.router.navigate(["/r-scolarite/view-ues"],t):this.router.navigate(["/dga/view-ues"],t)}getPermission(){let e=U()?.scolarite;return e?(console.log(e,"autorize"),!0):!1}goBack(){window.history.back()}static{this.\u0275fac=function(t){return new(t||n)(m(S),m(T),m(y),m(b))}}static{this.\u0275cmp=x({type:n,selectors:[["app-class-archive"]],decls:5,vars:2,consts:[[1,"back-button",3,"click"],[3,"icon"],[1,"grid-container"],["class","grid-item",4,"ngFor","ngForOf"],[1,"grid-item"],[1,"section-folder"],[1,"class-folder"],[1,"icon-folder"],[1,"icons-style",3,"icon"],[1,"info-folder"],[1,"title"],[1,"effectif"],[1,"btn-container"],[3,"click"],[4,"ngIf"],["class","views",4,"ngIf"],[1,"views"]],template:function(t,i){t&1&&(r(0,"main")(1,"div",0),p("click",function(){return i.goBack()}),_(2,"fa-icon",1),o(),r(3,"div",2),g(4,W,15,5,"div",3),o()()),t&2&&(s(2),c("icon",i.icons.back),s(2),c("ngForOf",i.classesArchives))},dependencies:[C,F,w],styles:[".col-12[_ngcontent-%COMP%]{padding:0 10px}button[_ngcontent-%COMP%]{color:var(--secondary);margin-top:20px;background-color:var(--primary);border:none}button[_ngcontent-%COMP%]:hover{background-color:var(--secondary);color:var(--primary)}a[_ngcontent-%COMP%]{text-decoration:none}.class-folder[_ngcontent-%COMP%]{flex-wrap:wrap;display:flex;justify-content:flex-start;align-items:center;gap:10px}.effectif[_ngcontent-%COMP%]{font-size:14px;font-weight:400!important}.icons-style[_ngcontent-%COMP%]{color:#0a0a0a;font-size:2.7rem}.info-folder[_ngcontent-%COMP%]   p[_ngcontent-%COMP%]{margin:0;font-weight:700;font-size:14px}.info-folder[_ngcontent-%COMP%]{text-align:left}.info-folder[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{text-decoration:none;display:flex;gap:8px;margin:0;cursor:pointer;color:var(--primary);font-size:14px}.info-folder[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{color:var(--secondary);background-color:var(--primary);padding:2px;border-radius:5px;width:100px}.info-folder[_ngcontent-%COMP%]   .title[_ngcontent-%COMP%]{white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.grid-container[_ngcontent-%COMP%]{padding:10px;display:grid;grid-template-columns:repeat(4,1fr);grid-gap:10px}.grid-item[_ngcontent-%COMP%]{position:relative;text-align:center}h4[_ngcontent-%COMP%]{background-color:var(--primary);color:var(--secondary);padding:10px;font-size:16px}.btn-container[_ngcontent-%COMP%]   span[_ngcontent-%COMP%]{padding:5px;border-radius:4px}.btn-container[_ngcontent-%COMP%]   span[_ngcontent-%COMP%]:hover{color:var(--primary);cursor:pointer;box-shadow:0 4px 8px #00000026}.btn-container[_ngcontent-%COMP%]{display:flex;gap:10px;margin-top:5px;align-items:center}.views[_ngcontent-%COMP%]   li[_ngcontent-%COMP%]{text-align:left;padding:5px;color:var(--primary);border-bottom:1px solid var(--tertiary);cursor:pointer}.views[_ngcontent-%COMP%]   li[_ngcontent-%COMP%]:hover{color:var(--secondary)}.views[_ngcontent-%COMP%]{background-color:#fff;position:absolute;top:80px;left:11px;border:1px solid #ddd;padding:8px;box-shadow:0 4px 8px #00000026;z-index:1000}"]})}}return n})();export{ee as a,ne as b};
