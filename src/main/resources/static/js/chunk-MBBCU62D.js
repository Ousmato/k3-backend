import{a as k}from"./chunk-MMUNQ446.js";import{a as X}from"./chunk-UJYHWII2.js";import{A as u,C as l,D as j,Db as N,Fa as U,Ga as M,Ha as L,I as a,Ia as q,J as o,K as _,L as f,M as g,N as O,Na as B,O as C,Oa as G,P as m,Qa as W,Ta as J,U as c,V as D,W as b,X as P,Za as w,ma as A,na as I,o as F,pa as R,qb as K,r as E,s as S,vb as Q,w as $,x as r,y as p,za as T}from"./chunk-Q3UIBATL.js";function oe(t,d){if(t&1&&(f(0),a(1,"td",7),c(2),o(),g()),t&2){let e=d.$implicit;r(2),D(e.toUpperCase())}}function re(t,d){if(t&1&&(f(0),a(1,"span",17),c(2),o(),g()),t&2){let e=m().$implicit;r(2),P(" ",e.classe.idFiliere==null||e.classe.idFiliere.idNiveau==null?null:e.classe.idFiliere.idNiveau.nom," (",e.classe.idFiliere==null||e.classe.idFiliere.idFiliere==null?null:e.classe.idFiliere.idFiliere.nomFiliere,") ")}}function ae(t,d){if(t&1&&(f(0),u(1,re,3,2,"ng-container",9),g()),t&2){let e=d.$implicit,n=m(2).$implicit;r(),l("ngIf",n.idEmplois.id===e.emploi.id)}}function se(t,d){if(t&1){let e=O();f(0),a(1,"div",10)(2,"div",11)(3,"strong"),c(4),o(),_(5,"br"),c(6),_(7,"br"),c(8),o(),a(9,"div",12)(10,"span",13),c(11),o(),a(12,"div",14),f(13),a(14,"button",15),C("click",function(){E(e);let s=m().$implicit,i=m(2);return S(i.change_observation(s.id))}),c(15),_(16,"img",16),o(),g(),o(),u(17,ae,2,1,"ng-container",6),o()(),g()}if(t&2){let e=m().$implicit,n=m(2);r(4),P("",n.teacher.nom," ",n.teacher.prenom,""),r(2),P(" ",e.heureDebut," - ",e.heureFin," "),r(2),b(" ",e.idModule.nomModule," "),r(3),b(" ",e.date_string,""),r(4),b(" ",e.observation?"Present":"Absent"," "),r(),l("src",e.observation?"assets/attendance-icon.svg":"assets/remove-cancel-user-icon.svg",$),r(),l("ngForOf",n.empois_class)}}function ce(t,d){if(t&1&&(f(0),u(1,se,18,9,"ng-container",9),g()),t&2){let e=d.$implicit,n=m().$implicit;r(),l("ngIf",n.day.toUpperCase()==e.jour)}}function le(t,d){if(t&1&&(f(0),a(1,"td",8),u(2,ce,2,1,"ng-container",6),o(),g()),t&2){let e=m();r(2),l("ngForOf",e.seances)}}var ve=(()=>{class t{constructor(e,n,s,i,h,x,y){this.teacherService=e,this.pageTitle=n,this.emploisService=s,this.fb=i,this.icons=h,this.router=x,this.datePipe=y,this.emplois=[],this.classes=[],this.seances=[],this.hoursList=[],this.dates_emplois=[],this.datesWithDaysTest=[],this.isPresent=!1,this.empois_class=[],this.day_of_head=["Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi"]}ngOnInit(){this.loadDetail(),this.presen_form=this.fb.group({idSeance:[[]]})}loadDetail(){this.router.queryParams.subscribe(e=>{let n=e.id;this.teacherService.getTeacherPresence(n).subscribe(s=>{this.detail_teacher=s,this.teacher=this.detail_teacher.teacher,this.classes=this.detail_teacher.classRoom,this.seances=this.detail_teacher.seances,console.log(this.seances,"seances"),this.detail_teacher.emplois.forEach(i=>{if(!this.emplois.includes(i)){let h=i.dateDebut,x=i.dateFin;console.log(this.datesWithDaysTest,"date- tes")}}),this.detail_teacher.seances.forEach(i=>{console.log(i.observation,"observation");let h;i.heureDebut=i.heureDebut.slice(0,5),i.heureFin=i.heureFin.slice(0,5),i.date=new Date(i.date),h=i.idEmplois.id;let x=this.datePipe.transform(i.date,"dd, MMMM yyyy","fr-FR");i.date_string=x;let y=this.datePipe.transform(i.date,"EEEE","fr-FR")?.toUpperCase();i.jour=y;let H=`${i.heureDebut} - ${i.heureFin}`;this.hoursList.includes(H)||this.hoursList.push(H);let V=s.emplois.find(v=>v.id===h),z=s.classRoom.find(v=>v.id===V?.idClasse.id);this.empois_class.some(v=>v.emploi.id===V.id&&v.classe.id===z.id)||this.empois_class.push({emploi:V,classe:z})})})})}currentDay(e){e=e.toUpperCase();let n=new Date,s=this.datePipe.transform(n,"EEEE","fr-FR")?.toUpperCase();return e===s}change_observation(e){let s={idSeance:this.seances.find(i=>i.id===e)};this.teacherService.chage_observation(s).subscribe({next:i=>{this.pageTitle.showSuccessToast(i.message),this.loadDetail()},error:i=>{this.pageTitle.showErrorToast(i.error.message)}})}goBack(){window.history.back()}static{this.\u0275fac=function(n){return new(n||t)(p(k),p(N),p(X),p(w),p(K),p(T),p(R))}}static{this.\u0275cmp=F({type:t,selectors:[["app-enseignant-pr-details"]],decls:14,vars:3,consts:[[1,"back-button",3,"click"],[3,"icon"],[1,"content"],["id","idTable",1,"section-table"],[1,"table","table-bordered"],[1,"horaire"],[4,"ngFor","ngForOf"],["scope","col"],["scope","col td-classe"],[4,"ngIf"],[1,"seance-content"],[1,"childre-1"],[1,"childre-2"],["id","date"],[1,"chil"],["type","submit",1,"btn",3,"click"],[3,"src"],["id","classe"]],template:function(n,s){n&1&&(a(0,"main")(1,"div",0),C("click",function(){return s.goBack()}),_(2,"fa-icon",1),o(),a(3,"section",2)(4,"div",3)(5,"table",4)(6,"thead")(7,"tr")(8,"th",5),c(9,"Horaires"),o(),u(10,oe,3,1,"ng-container",6),o()(),a(11,"tbody")(12,"tr"),u(13,le,3,1,"ng-container",6),o()()()()()()),n&2&&(r(2),l("icon",s.icons.back),r(8),l("ngForOf",s.day_of_head),r(3),l("ngForOf",s.datesWithDaysTest))},dependencies:[A,I,Q],styles:["main[_ngcontent-%COMP%]{padding:20px;width:100%}.content[_ngcontent-%COMP%]{display:flex;gap:20px}.childre-2[_ngcontent-%COMP%]{display:flex;flex-direction:column;justify-content:end;align-items:end}#date[_ngcontent-%COMP%]{font-size:13px;color:#00f;margin-bottom:10px}#classe[_ngcontent-%COMP%]{font-size:11px}img[_ngcontent-%COMP%]{height:30px;width:auto}img[_ngcontent-%COMP%]:hover{border:1px solid var(--primary);border-radius:4px;padding:5px}span[_ngcontent-%COMP%]{font-size:12px}.month-contenaire[_ngcontent-%COMP%]{display:flex;gap:20px}.section-table[_ngcontent-%COMP%]{width:100%}.seance-content[_ngcontent-%COMP%]{width:100%;display:flex;justify-content:space-between;align-items:flex-end;border-bottom:1px solid #f1f1f1;padding-bottom:10px;padding-top:10px}.text-left[_ngcontent-%COMP%], .horaire[_ngcontent-%COMP%]{display:none}.section-head[_ngcontent-%COMP%]{padding:10px 20px 10px 10px;width:100%;display:flex;justify-content:space-between;height:50px;background-color:#f4f4f4}p[_ngcontent-%COMP%]{font-size:20px;font-weight:500}.td-classe[_ngcontent-%COMP%]{border-bottom:1px solid #ccc!important}.btn[_ngcontent-%COMP%]{font-size:10px;margin-left:20px}#btn-submit[_ngcontent-%COMP%]{margin-top:10px}.input-time[_ngcontent-%COMP%]{display:flex;justify-content:space-between}.hidden[_ngcontent-%COMP%]{display:none}section[_ngcontent-%COMP%]{border:none}@media (max-width: 768px){.grid-item[_ngcontent-%COMP%]{flex:1 1 calc(50% - 40px)}}@media (max-width: 480px){.grid-item[_ngcontent-%COMP%]{flex:1 1 100%}}.container[_ngcontent-%COMP%]{display:flex;flex-direction:column;gap:10px}.empModuleName[_ngcontent-%COMP%]{font-size:12px;font-weight:400}.back-button[_ngcontent-%COMP%]{width:30px;height:30px;display:flex;align-items:center;padding:8px;font-size:20px;margin:10px 0}.back-button[_ngcontent-%COMP%]:hover{background-color:var(--tertiary);color:var(--primary);border:none;border-radius:50%}"]})}}return t})();function de(t,d){t&1&&_(0,"input",14)}function pe(t,d){t&1&&_(0,"input",15)}function me(t,d){t&1&&(a(0,"div",16)(1,"span",17),c(2,"Le montant doit \xEAtre sup\xE9rieur ou \xE9gal \xE0 1000 FCFA."),o()())}function ue(t,d){t&1&&(a(0,"div",16)(1,"span",17),c(2,"Montant trop volumuneux."),o()())}function _e(t,d){if(t&1){let e=O();a(0,"div",2),_(1,"div",3),a(2,"form",4)(3,"table",5)(4,"thead")(5,"tr")(6,"th"),c(7,"Date"),o(),a(8,"th"),c(9,"Nom des classes"),o(),a(10,"th"),c(11,"Nombre d'heures effectu\xE9es"),o(),a(12,"th"),c(13,"Prix/heure"),o(),a(14,"th"),c(15,"Montant"),o()()(),a(16,"tbody")(17,"tr")(18,"td"),c(19),o(),a(20,"td"),c(21),o(),a(22,"td"),c(23),o(),a(24,"td"),u(25,de,1,0,"input",6)(26,pe,1,0,"input",7),a(27,"input",8),C("input",function(s){E(e);let i=m();return S(i.loadInputValue(s,i.presence))}),o(),u(28,me,3,0,"div",9),o(),a(29,"td"),_(30,"input",10),u(31,ue,3,0,"div",9),o()()()(),a(32,"div",11)(33,"a",12),C("click",function(){E(e);let s=m();return S(s.close_modal())}),c(34,"Annuler"),o(),a(35,"button",13),c(36,"Valider"),o()()()()}if(t&2){let e=m();r(2),l("formGroup",e.form_paie),r(17),D(e.presence==null?null:e.presence.date),r(2),P(" ",e.presence==null||e.presence.idSeance==null||e.presence.idSeance.idEmplois==null||e.presence.idSeance.idEmplois.idClasse==null||e.presence.idSeance.idEmplois.idClasse.idFiliere==null||e.presence.idSeance.idEmplois.idClasse.idFiliere.idNiveau==null?null:e.presence.idSeance.idEmplois.idClasse.idFiliere.idNiveau.nom," (",e.presence==null||e.presence.idSeance==null||e.presence.idSeance.idEmplois==null||e.presence.idSeance.idEmplois.idClasse==null||e.presence.idSeance.idEmplois.idClasse.idFiliere==null||e.presence.idSeance.idEmplois.idClasse.idFiliere.idFiliere==null?null:e.presence.idSeance.idEmplois.idClasse.idFiliere.idFiliere.nomFiliere,") "),r(2),b("",e.presence==null?null:e.presence.heure," Heure(s)"),r(2),l("ngIf",!1),r(),l("ngIf",!1),r(),j("is-invalid",e.form_paie.get("coutHeure").invalid&&e.form_paie.get("coutHeure").touched),r(),l("ngIf",e.form_paie.get("coutHeure").invalid&&e.form_paie.get("coutHeure").touched),r(2),j("is-invalid",e.form_paie.get("montant").invalid&&e.form_paie.get("montant").touched),r(),l("ngIf",e.form_paie.get("montant").invalid&&e.form_paie.get("montant").touched)}}function he(t,d){t&1&&_(0,"div",18)}var Pe=(()=>{class t{constructor(e,n,s,i){this.teacherService=e,this.pageTitle=n,this.fb=s,this.root=i,this.show_modal=!0,this.diff_heure=0}ngOnInit(){this.load_form(),this.getTeacherPresence()}load_form(){this.form_paie=this.fb.group({coutHeure:["",[M.required,M.min(1e3)]],nbreHeures:[""],montant:["",[M.required,M.max(5e5)]],idPresenceTeachers:[""]})}getTeacherPresence(){this.root.queryParams.subscribe(e=>{this.idSeance=e.id}),this.teacherService.getPresence_by_seance(this.idSeance).subscribe(e=>{this.presence=e,console.log(this.presence,"presence"),this.load_diff(this.presence)})}load_diff(e){let n=new Date("1970-01-01T"+e.idSeance.heureDebut),i=new Date("1970-01-01T"+e.idSeance.heureFin).getTime()-n.getTime(),h=Math.floor(i/(1e3*60)),x=Math.floor(h/60),y=h%60;e.heure=x,e.munite=y,this.diff_heure=e.heure,console.log(this.diff_heure,"heure diff")}loadInputValue(e,n){let s=this.form_paie.get("coutHeure").value,i=n.heure,h=+s*+i;this.form_paie.get("montant")?.setValue(h+" FCFA"),this.form_paie.get("idPresenceTeachers")?.setValue(n.idSeance.id),this.form_paie.get("nbreHeures")?.setValue(n.heure)}close_modal(){this.show_modal=!1,window.history.back()}static{this.\u0275fac=function(n){return new(n||t)(p(k),p(N),p(w),p(T))}}static{this.\u0275cmp=F({type:t,selectors:[["app-fiche-de-paie"]],decls:3,vars:2,consts:[["class","contente",4,"ngIf"],["class","overlay",4,"ngIf"],[1,"contente"],[1,"cont-head"],[3,"formGroup"],[1,"table","table-bordered"],["type","number","formControlName","nbreHeures",4,"ngIf"],["type","number","formControlName","idPresenceTeachers",4,"ngIf"],["type","number","formControlName","coutHeure","placeholder","Cout unitaire...",1,"form-control",3,"input"],["class","invalid-feedback",4,"ngIf"],["readonly","","type","text","formControlName","montant","placeholder","Mont calculer...",1,"form-control"],[1,"button-cash"],[3,"click"],["type","submit"],["type","number","formControlName","nbreHeures"],["type","number","formControlName","idPresenceTeachers"],[1,"invalid-feedback"],[1,"label-error"],[1,"overlay"]],template:function(n,s){n&1&&(a(0,"main"),u(1,_e,37,13,"div",0)(2,he,1,0,"div",1),o()),n&2&&(r(),l("ngIf",s.show_modal),r(),l("ngIf",s.show_modal))},dependencies:[I,B,U,G,L,q,W,J],styles:[".overlay[_ngcontent-%COMP%]{position:fixed;top:0;left:0;width:100%;height:100%;z-index:1000;background:#00000080}table[_ngcontent-%COMP%]{font-size:14px}.label-error[_ngcontent-%COMP%]{color:red}span[_ngcontent-%COMP%]{font-size:12px}.contente[_ngcontent-%COMP%]{width:90%;border-radius:5px;box-shadow:0 4px 8px #00000026;z-index:1001;position:absolute;top:15%;left:0;background-color:#fff}.cont-head[_ngcontent-%COMP%]{height:40px;background-color:var(--primary);border-top-left-radius:5px;border-top-right-radius:5px}form[_ngcontent-%COMP%]{padding:20px}.button-cash[_ngcontent-%COMP%]{display:flex;gap:20px;justify-content:end}.button-cash[_ngcontent-%COMP%]   button[_ngcontent-%COMP%]{border:none;display:flex;gap:8px;margin-top:8px;padding:5px;border-radius:5px;color:var(--secondary);cursor:pointer}.button-cash[_ngcontent-%COMP%]   button[_ngcontent-%COMP%]:hover{background-color:var(--secondary);color:var(--primary)}.button-cash[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]{text-decoration:none;border:none;margin-top:8px;padding:5px;border-radius:5px;background-color:var(--secondary);cursor:pointer}.button-cash[_ngcontent-%COMP%]   a[_ngcontent-%COMP%]:hover{background-color:var(--primary);color:var(--secondary)}"]})}}return t})();export{ve as a,Pe as b};
