(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-34c152f2"],{"1da1":function(t,e,r){"use strict";r.d(e,"a",(function(){return o}));r("d3b7");function n(t,e,r,n,o,a,i){try{var u=t[a](i),c=u.value}catch(s){return void r(s)}u.done?e(c):Promise.resolve(c).then(n,o)}function o(t){return function(){var e=this,r=arguments;return new Promise((function(o,a){var i=t.apply(e,r);function u(t){n(i,o,a,u,c,"next",t)}function c(t){n(i,o,a,u,c,"throw",t)}u(void 0)}))}}},"73cf":function(t,e,r){"use strict";r.r(e);var n=r("7a23"),o=Object(n["D"])("data-v-f15fd7ce");Object(n["t"])("data-v-f15fd7ce");var a=Object(n["g"])("Register"),i=Object(n["g"])("Register");Object(n["r"])();var u=o((function(t,e,r,u,c,s){var l=Object(n["x"])("ion-title"),h=Object(n["x"])("ion-toolbar"),f=Object(n["x"])("ion-header"),d=Object(n["x"])("ion-input"),p=Object(n["x"])("ion-button"),v=Object(n["x"])("ion-content"),m=Object(n["x"])("ion-page");return Object(n["q"])(),Object(n["e"])(m,null,{default:o((function(){return[Object(n["h"])(f,null,{default:o((function(){return[Object(n["h"])(h,null,{default:o((function(){return[Object(n["h"])(l,null,{default:o((function(){return[a]})),_:1})]})),_:1})]})),_:1}),Object(n["h"])(v,{fullscreen:!0},{default:o((function(){return[Object(n["h"])(d,{placeholder:"Enter username",onIonChange:t.validate,modelValue:t.username,"onUpdate:modelValue":e[1]||(e[1]=function(e){return t.username=e})},null,8,["onIonChange","modelValue"]),Object(n["h"])(d,{placeholder:"Enter password",onIonChange:t.validate,modelValue:t.password,"onUpdate:modelValue":e[2]||(e[2]=function(e){return t.password=e}),type:"password"},null,8,["onIonChange","modelValue"]),Object(n["h"])(d,{placeholder:"Repeat password",onIonChange:t.validate,modelValue:t.repeatPassword,"onUpdate:modelValue":e[3]||(e[3]=function(e){return t.repeatPassword=e}),type:"password"},null,8,["onIonChange","modelValue"]),Object(n["h"])(d,{placeholder:"Enter email",onIonChange:t.validate,modelValue:t.email,"onUpdate:modelValue":e[4]||(e[4]=function(e){return t.email=e})},null,8,["onIonChange","modelValue"]),Object(n["h"])(p,{disabled:t.disabled,expand:"full",onClick:t.register},{default:o((function(){return[i]})),_:1},8,["disabled","onClick"])]})),_:1})]})),_:1})})),c=r("1da1"),s=(r("96cf"),r("d867")),l=r("ff79"),h=r("6c02"),f=r("bc3a"),d=r.n(f),p=Object(n["i"])({components:{IonInput:s["n"],IonButton:s["b"],IonContent:s["i"],IonPage:s["r"],IonToolbar:s["z"],IonHeader:s["j"],IonTitle:s["y"]},setup:function(){var t=Object(h["i"])();return{warning:l["r"],router:t}},methods:{failedRegisterToast:function(){return Object(c["a"])(regeneratorRuntime.mark((function t(){var e;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,s["C"].create({message:"Your registration data was incorrent. Try again.",duration:2e3});case 2:return e=t.sent,t.abrupt("return",e.present());case 4:case"end":return t.stop()}}),t)})))()},validate:function(){""!=this.username&&""!=this.password&&""!=this.repeatPassword&&this.repeatPassword==this.password&&""!=this.email?this.disabled=!1:this.disabled=!0},register:function(){var t=this;return Object(c["a"])(regeneratorRuntime.mark((function e(){var r;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:r={username:t.username,email:t.email,password:t.password,passwordConfirm:t.repeatPassword},d.a.post("api/users",r).then((function(e){t.username="",t.password="",t.repeatPassword="",t.email="",t.disabled=!0,t.router.push("/")})).catch((function(e){console.error(e),t.failedRegisterToast()}));case 2:case"end":return e.stop()}}),e)})))()}},data:function(){return{username:"",password:"",repeatPassword:"",email:"",disabled:!0}}}),v=r("6b0d"),m=r.n(v);const y=m()(p,[["render",u],["__scopeId","data-v-f15fd7ce"]]);e["default"]=y},"96cf":function(t,e,r){var n=function(t){"use strict";var e,r=Object.prototype,n=r.hasOwnProperty,o="function"===typeof Symbol?Symbol:{},a=o.iterator||"@@iterator",i=o.asyncIterator||"@@asyncIterator",u=o.toStringTag||"@@toStringTag";function c(t,e,r){return Object.defineProperty(t,e,{value:r,enumerable:!0,configurable:!0,writable:!0}),t[e]}try{c({},"")}catch(R){c=function(t,e,r){return t[e]=r}}function s(t,e,r,n){var o=e&&e.prototype instanceof m?e:m,a=Object.create(o.prototype),i=new k(n||[]);return a._invoke=E(t,r,i),a}function l(t,e,r){try{return{type:"normal",arg:t.call(e,r)}}catch(R){return{type:"throw",arg:R}}}t.wrap=s;var h="suspendedStart",f="suspendedYield",d="executing",p="completed",v={};function m(){}function y(){}function g(){}var w={};w[a]=function(){return this};var b=Object.getPrototypeOf,j=b&&b(b(C([])));j&&j!==r&&n.call(j,a)&&(w=j);var O=g.prototype=m.prototype=Object.create(w);function x(t){["next","throw","return"].forEach((function(e){c(t,e,(function(t){return this._invoke(e,t)}))}))}function L(t,e){function r(o,a,i,u){var c=l(t[o],t,a);if("throw"!==c.type){var s=c.arg,h=s.value;return h&&"object"===typeof h&&n.call(h,"__await")?e.resolve(h.__await).then((function(t){r("next",t,i,u)}),(function(t){r("throw",t,i,u)})):e.resolve(h).then((function(t){s.value=t,i(s)}),(function(t){return r("throw",t,i,u)}))}u(c.arg)}var o;function a(t,n){function a(){return new e((function(e,o){r(t,n,e,o)}))}return o=o?o.then(a,a):a()}this._invoke=a}function E(t,e,r){var n=h;return function(o,a){if(n===d)throw new Error("Generator is already running");if(n===p){if("throw"===o)throw a;return V()}r.method=o,r.arg=a;while(1){var i=r.delegate;if(i){var u=_(i,r);if(u){if(u===v)continue;return u}}if("next"===r.method)r.sent=r._sent=r.arg;else if("throw"===r.method){if(n===h)throw n=p,r.arg;r.dispatchException(r.arg)}else"return"===r.method&&r.abrupt("return",r.arg);n=d;var c=l(t,e,r);if("normal"===c.type){if(n=r.done?p:f,c.arg===v)continue;return{value:c.arg,done:r.done}}"throw"===c.type&&(n=p,r.method="throw",r.arg=c.arg)}}}function _(t,r){var n=t.iterator[r.method];if(n===e){if(r.delegate=null,"throw"===r.method){if(t.iterator["return"]&&(r.method="return",r.arg=e,_(t,r),"throw"===r.method))return v;r.method="throw",r.arg=new TypeError("The iterator does not provide a 'throw' method")}return v}var o=l(n,t.iterator,r.arg);if("throw"===o.type)return r.method="throw",r.arg=o.arg,r.delegate=null,v;var a=o.arg;return a?a.done?(r[t.resultName]=a.value,r.next=t.nextLoc,"return"!==r.method&&(r.method="next",r.arg=e),r.delegate=null,v):a:(r.method="throw",r.arg=new TypeError("iterator result is not an object"),r.delegate=null,v)}function I(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function P(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function k(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(I,this),this.reset(!0)}function C(t){if(t){var r=t[a];if(r)return r.call(t);if("function"===typeof t.next)return t;if(!isNaN(t.length)){var o=-1,i=function r(){while(++o<t.length)if(n.call(t,o))return r.value=t[o],r.done=!1,r;return r.value=e,r.done=!0,r};return i.next=i}}return{next:V}}function V(){return{value:e,done:!0}}return y.prototype=O.constructor=g,g.constructor=y,y.displayName=c(g,u,"GeneratorFunction"),t.isGeneratorFunction=function(t){var e="function"===typeof t&&t.constructor;return!!e&&(e===y||"GeneratorFunction"===(e.displayName||e.name))},t.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,g):(t.__proto__=g,c(t,u,"GeneratorFunction")),t.prototype=Object.create(O),t},t.awrap=function(t){return{__await:t}},x(L.prototype),L.prototype[i]=function(){return this},t.AsyncIterator=L,t.async=function(e,r,n,o,a){void 0===a&&(a=Promise);var i=new L(s(e,r,n,o),a);return t.isGeneratorFunction(r)?i:i.next().then((function(t){return t.done?t.value:i.next()}))},x(O),c(O,u,"Generator"),O[a]=function(){return this},O.toString=function(){return"[object Generator]"},t.keys=function(t){var e=[];for(var r in t)e.push(r);return e.reverse(),function r(){while(e.length){var n=e.pop();if(n in t)return r.value=n,r.done=!1,r}return r.done=!0,r}},t.values=C,k.prototype={constructor:k,reset:function(t){if(this.prev=0,this.next=0,this.sent=this._sent=e,this.done=!1,this.delegate=null,this.method="next",this.arg=e,this.tryEntries.forEach(P),!t)for(var r in this)"t"===r.charAt(0)&&n.call(this,r)&&!isNaN(+r.slice(1))&&(this[r]=e)},stop:function(){this.done=!0;var t=this.tryEntries[0],e=t.completion;if("throw"===e.type)throw e.arg;return this.rval},dispatchException:function(t){if(this.done)throw t;var r=this;function o(n,o){return u.type="throw",u.arg=t,r.next=n,o&&(r.method="next",r.arg=e),!!o}for(var a=this.tryEntries.length-1;a>=0;--a){var i=this.tryEntries[a],u=i.completion;if("root"===i.tryLoc)return o("end");if(i.tryLoc<=this.prev){var c=n.call(i,"catchLoc"),s=n.call(i,"finallyLoc");if(c&&s){if(this.prev<i.catchLoc)return o(i.catchLoc,!0);if(this.prev<i.finallyLoc)return o(i.finallyLoc)}else if(c){if(this.prev<i.catchLoc)return o(i.catchLoc,!0)}else{if(!s)throw new Error("try statement without catch or finally");if(this.prev<i.finallyLoc)return o(i.finallyLoc)}}}},abrupt:function(t,e){for(var r=this.tryEntries.length-1;r>=0;--r){var o=this.tryEntries[r];if(o.tryLoc<=this.prev&&n.call(o,"finallyLoc")&&this.prev<o.finallyLoc){var a=o;break}}a&&("break"===t||"continue"===t)&&a.tryLoc<=e&&e<=a.finallyLoc&&(a=null);var i=a?a.completion:{};return i.type=t,i.arg=e,a?(this.method="next",this.next=a.finallyLoc,v):this.complete(i)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),v},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.finallyLoc===t)return this.complete(r.completion,r.afterLoc),P(r),v}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.tryLoc===t){var n=r.completion;if("throw"===n.type){var o=n.arg;P(r)}return o}}throw new Error("illegal catch attempt")},delegateYield:function(t,r,n){return this.delegate={iterator:C(t),resultName:r,nextLoc:n},"next"===this.method&&(this.arg=e),v}},t}(t.exports);try{regeneratorRuntime=n}catch(o){Function("r","regeneratorRuntime = r")(n)}}}]);
//# sourceMappingURL=chunk-34c152f2.78fd163a.js.map