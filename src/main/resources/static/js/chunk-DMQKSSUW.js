import{cb as o,k as s,m as e,sa as n}from"./chunk-Q3UIBATL.js";var b=(()=>{class i{constructor(t){this.http=t,this.baseUrl=`${o.apiUrl}api-subscribe/`}getInscriptionsIdClasse(t,r){return this.http.get(`${this.baseUrl}subscribe-by-class-id/${t}/${r}`)}getListInscriptionByIdGroup(t){return this.http.get(this.baseUrl+"list-subscribe-by-group-id/"+t)}getListInscriptionByIdEmploi(t){return this.http.get(this.baseUrl+"list-subscribe-by-emploi-id/"+t)}annulerDepot(t){return this.http.get(this.baseUrl+"annuler-depot/"+t)}getInscriptionById(t){return this.http.get(this.baseUrl+"inscription-by-id/"+t)}static{this.\u0275fac=function(r){return new(r||i)(e(n))}}static{this.\u0275prov=s({token:i,factory:i.\u0275fac,providedIn:"root"})}}return i})();export{b as a};
