(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-1bff95de"],{"1ad0":function(e,t,o){"use strict";o.r(t),o.d(t,"ion_modal",(function(){return O}));var i=o("f530"),a=o("6d28"),r=o("7d8d"),n=o("6ad3"),s=o("32c0"),d=o("feca"),l=o("9897"),c=o("ef7a"),m=o("41b3"),p=o("391c"),h=o("61cf"),f=(o("5ea3"),o("347a"),{MIN_PRESENTING_SCALE:.93}),u=function(e,t,o){var i=e.offsetHeight,a=!1,r=function(e){var t=e.event.target;if(null===t||!t.closest)return!0;var o=t.closest("ion-content");return null===o},n=function(){t.progressStart(!0,a?1:0)},s=function(e){var o=Object(h["j"])(1e-4,e.deltaY/i,.9999);t.progressStep(o)},d=function(e){var r=e.velocityY,n=Object(h["j"])(1e-4,e.deltaY/i,.9999),s=(e.deltaY+1e3*r)/i,d=s>=.5,c=d?-.001:.001;d?(t.easing("cubic-bezier(0.32, 0.72, 0, 1)"),c+=Object(m["a"])([0,0],[.32,.72],[0,1],[1,1],n)[0]):(t.easing("cubic-bezier(1, 0, 0.68, 0.28)"),c+=Object(m["a"])([0,0],[1,0],[.68,.28],[1,1],n)[0]);var p=b(d?n*i:(1-n)*i,r);a=d,l.enable(!1),t.onFinish((function(){d||l.enable(!0)})).progressEnd(d?1:0,c,p),d&&o()},l=Object(p["createGesture"])({el:e,gestureName:"modalSwipeToClose",gesturePriority:40,direction:"y",threshold:10,canStart:r,onStart:n,onMove:s,onEnd:d});return l},b=function(e,t){return Object(h["j"])(400,e/Math.abs(1.1*t),500)},w=function(e,t){var o=Object(c["a"])().addElement(e.querySelector("ion-backdrop")).fromTo("opacity",.01,"var(--backdrop-opacity)").beforeStyles({"pointer-events":"none"}).afterClearStyles(["pointer-events"]),i=Object(c["a"])().addElement(e.querySelectorAll(".modal-wrapper, .modal-shadow")).beforeStyles({opacity:1}).fromTo("transform","translateY(100vh)","translateY(0vh)"),a=Object(c["a"])().addElement(e).easing("cubic-bezier(0.32,0.72,0,1)").duration(500).addAnimation(i);if(t){var r=window.innerWidth<768,n="ION-MODAL"===t.tagName&&void 0!==t.presentingElement,s=Object(c["a"])().beforeStyles({transform:"translateY(0)","transform-origin":"top center",overflow:"hidden"}),d=document.body;if(r){var l=CSS.supports("width","max(0px, 1px)")?"max(30px, var(--ion-safe-area-top))":"30px",m=n?"-10px":l,p=f.MIN_PRESENTING_SCALE,h="translateY("+m+") scale("+p+")";s.afterStyles({transform:h}).beforeAddWrite((function(){return d.style.setProperty("background-color","black")})).addElement(t).keyframes([{offset:0,filter:"contrast(1)",transform:"translateY(0px) scale(1)",borderRadius:"0px"},{offset:1,filter:"contrast(0.85)",transform:h,borderRadius:"10px 10px 0 0"}]),a.addAnimation(s)}else if(a.addAnimation(o),n){p=n?f.MIN_PRESENTING_SCALE:1,h="translateY(-10px) scale("+p+")";s.afterStyles({transform:h}).addElement(t.querySelector(".modal-wrapper")).keyframes([{offset:0,filter:"contrast(1)",transform:"translateY(0) scale(1)"},{offset:1,filter:"contrast(0.85)",transform:h}]);var u=Object(c["a"])().afterStyles({transform:h}).addElement(t.querySelector(".modal-shadow")).keyframes([{offset:0,opacity:"1",transform:"translateY(0) scale(1)"},{offset:1,opacity:"0",transform:h}]);a.addAnimation([s,u])}else i.fromTo("opacity","0","1")}else a.addAnimation(o);return a},v=function(e,t,o){void 0===o&&(o=500);var i=Object(c["a"])().addElement(e.querySelector("ion-backdrop")).fromTo("opacity","var(--backdrop-opacity)",0),a=Object(c["a"])().addElement(e.querySelectorAll(".modal-wrapper, .modal-shadow")).beforeStyles({opacity:1}).fromTo("transform","translateY(0vh)","translateY(100vh)"),r=Object(c["a"])().addElement(e).easing("cubic-bezier(0.32,0.72,0,1)").duration(o).addAnimation(a);if(t){var n=window.innerWidth<768,s="ION-MODAL"===t.tagName&&void 0!==t.presentingElement,d=Object(c["a"])().beforeClearStyles(["transform"]).afterClearStyles(["transform"]).onFinish((function(e){if(1===e){t.style.setProperty("overflow","");var o=Array.from(l.querySelectorAll("ion-modal")).filter((function(e){return void 0!==e.presentingElement})).length;o<=1&&l.style.setProperty("background-color","")}})),l=document.body;if(n){var m=CSS.supports("width","max(0px, 1px)")?"max(30px, var(--ion-safe-area-top))":"30px",p=s?"-10px":m,h=f.MIN_PRESENTING_SCALE,u="translateY("+p+") scale("+h+")";d.addElement(t).keyframes([{offset:0,filter:"contrast(0.85)",transform:u,borderRadius:"10px 10px 0 0"},{offset:1,filter:"contrast(1)",transform:"translateY(0px) scale(1)",borderRadius:"0px"}]),r.addAnimation(d)}else if(r.addAnimation(i),s){h=s?f.MIN_PRESENTING_SCALE:1,u="translateY(-10px) scale("+h+")";d.addElement(t.querySelector(".modal-wrapper")).afterStyles({transform:"translate3d(0, 0, 0)"}).keyframes([{offset:0,filter:"contrast(0.85)",transform:u},{offset:1,filter:"contrast(1)",transform:"translateY(0) scale(1)"}]);var b=Object(c["a"])().addElement(t.querySelector(".modal-shadow")).afterStyles({transform:"translateY(0) scale(1)"}).keyframes([{offset:0,opacity:"0",transform:u},{offset:1,opacity:"1",transform:"translateY(0) scale(1)"}]);r.addAnimation([d,b])}else a.fromTo("opacity","1","0")}else r.addAnimation(i);return r},x=function(e){var t=Object(c["a"])(),o=Object(c["a"])(),i=Object(c["a"])();return o.addElement(e.querySelector("ion-backdrop")).fromTo("opacity",.01,"var(--backdrop-opacity)").beforeStyles({"pointer-events":"none"}).afterClearStyles(["pointer-events"]),i.addElement(e.querySelector(".modal-wrapper")).keyframes([{offset:0,opacity:.01,transform:"translateY(40px)"},{offset:1,opacity:1,transform:"translateY(0px)"}]),t.addElement(e).easing("cubic-bezier(0.36,0.66,0.04,1)").duration(280).addAnimation([o,i])},y=function(e){var t=Object(c["a"])(),o=Object(c["a"])(),i=Object(c["a"])(),a=e.querySelector(".modal-wrapper");return o.addElement(e.querySelector("ion-backdrop")).fromTo("opacity","var(--backdrop-opacity)",0),i.addElement(a).keyframes([{offset:0,opacity:.99,transform:"translateY(0px)"},{offset:1,opacity:0,transform:"translateY(40px)"}]),t.addElement(e).easing("cubic-bezier(0.47,0,0.745,0.715)").duration(200).addAnimation([o,i])},g=".sc-ion-modal-ios-h{--width:100%;--min-width:auto;--max-width:auto;--height:100%;--min-height:auto;--max-height:auto;--overflow:hidden;--border-radius:0;--border-width:0;--border-style:none;--border-color:transparent;--background:var(--ion-background-color, #fff);--box-shadow:none;--backdrop-opacity:0;left:0;right:0;top:0;bottom:0;display:-ms-flexbox;display:flex;position:absolute;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;outline:none;contain:strict}.overlay-hidden.sc-ion-modal-ios-h{display:none}.modal-wrapper.sc-ion-modal-ios,.modal-shadow.sc-ion-modal-ios{border-radius:var(--border-radius);width:var(--width);min-width:var(--min-width);max-width:var(--max-width);height:var(--height);min-height:var(--min-height);max-height:var(--max-height);border-width:var(--border-width);border-style:var(--border-style);border-color:var(--border-color);background:var(--background);-webkit-box-shadow:var(--box-shadow);box-shadow:var(--box-shadow);overflow:var(--overflow);z-index:10}.modal-shadow.sc-ion-modal-ios{position:absolute;background:transparent}@media only screen and (min-width: 768px) and (min-height: 600px){.sc-ion-modal-ios-h{--width:600px;--height:500px;--ion-safe-area-top:0px;--ion-safe-area-bottom:0px;--ion-safe-area-right:0px;--ion-safe-area-left:0px}}@media only screen and (min-width: 768px) and (min-height: 768px){.sc-ion-modal-ios-h{--width:600px;--height:600px}}.sc-ion-modal-ios-h:first-of-type{--backdrop-opacity:var(--ion-backdrop-opacity, 0.4)}@media only screen and (min-width: 768px) and (min-height: 600px){.sc-ion-modal-ios-h{--border-radius:10px}}.modal-wrapper.sc-ion-modal-ios{-webkit-transform:translate3d(0,  100%,  0);transform:translate3d(0,  100%,  0)}@media screen and (max-width: 767px){@supports (width: max(0px, 1px)){.modal-card.sc-ion-modal-ios-h{--height:calc(100% - max(30px, var(--ion-safe-area-top)) - 10px)}}@supports not (width: max(0px, 1px)){.modal-card.sc-ion-modal-ios-h{--height:calc(100% - 40px)}}.modal-card.sc-ion-modal-ios-h .modal-wrapper.sc-ion-modal-ios{border-top-left-radius:10px;border-top-right-radius:10px;border-bottom-right-radius:0;border-bottom-left-radius:0}[dir=rtl].sc-ion-modal-ios-h -no-combinator.modal-card.sc-ion-modal-ios-h .modal-wrapper.sc-ion-modal-ios,[dir=rtl] .sc-ion-modal-ios-h -no-combinator.modal-card.sc-ion-modal-ios-h .modal-wrapper.sc-ion-modal-ios,[dir=rtl].modal-card.sc-ion-modal-ios-h .modal-wrapper.sc-ion-modal-ios,[dir=rtl] .modal-card.sc-ion-modal-ios-h .modal-wrapper.sc-ion-modal-ios{border-top-left-radius:10px;border-top-right-radius:10px;border-bottom-right-radius:0;border-bottom-left-radius:0}.modal-card.sc-ion-modal-ios-h{--backdrop-opacity:0;--width:100%;-ms-flex-align:end;align-items:flex-end}.modal-card.sc-ion-modal-ios-h .modal-shadow.sc-ion-modal-ios{display:none}.modal-card.sc-ion-modal-ios-h ion-backdrop.sc-ion-modal-ios{pointer-events:none}}@media screen and (min-width: 768px){.modal-card.sc-ion-modal-ios-h{--width:calc(100% - 120px);--height:calc(100% - (120px + var(--ion-safe-area-top) + var(--ion-safe-area-bottom)));--max-width:720px;--max-height:1000px}.modal-card.sc-ion-modal-ios-h{--backdrop-opacity:0;-webkit-transition:all 0.5s ease-in-out;transition:all 0.5s ease-in-out}.modal-card.sc-ion-modal-ios-h:first-of-type{--backdrop-opacity:0.18}.modal-card.sc-ion-modal-ios-h .modal-shadow.sc-ion-modal-ios{-webkit-box-shadow:0px 0px 30px 10px rgba(0, 0, 0, 0.1);box-shadow:0px 0px 30px 10px rgba(0, 0, 0, 0.1)}}",j=".sc-ion-modal-md-h{--width:100%;--min-width:auto;--max-width:auto;--height:100%;--min-height:auto;--max-height:auto;--overflow:hidden;--border-radius:0;--border-width:0;--border-style:none;--border-color:transparent;--background:var(--ion-background-color, #fff);--box-shadow:none;--backdrop-opacity:0;left:0;right:0;top:0;bottom:0;display:-ms-flexbox;display:flex;position:absolute;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;outline:none;contain:strict}.overlay-hidden.sc-ion-modal-md-h{display:none}.modal-wrapper.sc-ion-modal-md,.modal-shadow.sc-ion-modal-md{border-radius:var(--border-radius);width:var(--width);min-width:var(--min-width);max-width:var(--max-width);height:var(--height);min-height:var(--min-height);max-height:var(--max-height);border-width:var(--border-width);border-style:var(--border-style);border-color:var(--border-color);background:var(--background);-webkit-box-shadow:var(--box-shadow);box-shadow:var(--box-shadow);overflow:var(--overflow);z-index:10}.modal-shadow.sc-ion-modal-md{position:absolute;background:transparent}@media only screen and (min-width: 768px) and (min-height: 600px){.sc-ion-modal-md-h{--width:600px;--height:500px;--ion-safe-area-top:0px;--ion-safe-area-bottom:0px;--ion-safe-area-right:0px;--ion-safe-area-left:0px}}@media only screen and (min-width: 768px) and (min-height: 768px){.sc-ion-modal-md-h{--width:600px;--height:600px}}.sc-ion-modal-md-h:first-of-type{--backdrop-opacity:var(--ion-backdrop-opacity, 0.32)}@media only screen and (min-width: 768px) and (min-height: 600px){.sc-ion-modal-md-h{--border-radius:2px}.sc-ion-modal-md-h:first-of-type{--box-shadow:0 28px 48px rgba(0, 0, 0, 0.4)}}.modal-wrapper.sc-ion-modal-md{-webkit-transform:translate3d(0,  40px,  0);transform:translate3d(0,  40px,  0);opacity:0.01}",O=function(){function e(e){var t=this;Object(a["o"])(this,e),this.didPresent=Object(a["g"])(this,"ionModalDidPresent",7),this.willPresent=Object(a["g"])(this,"ionModalWillPresent",7),this.willDismiss=Object(a["g"])(this,"ionModalWillDismiss",7),this.didDismiss=Object(a["g"])(this,"ionModalDidDismiss",7),this.gestureAnimationDismissing=!1,this.presented=!1,this.keyboardClose=!0,this.backdropDismiss=!0,this.showBackdrop=!0,this.animated=!0,this.swipeToClose=!1,this.onBackdropTap=function(){t.dismiss(void 0,s["a"])},this.onDismiss=function(e){e.stopPropagation(),e.preventDefault(),t.dismiss()},this.onLifecycle=function(e){var o=t.usersElement,i=k[e.type];if(o&&i){var a=new CustomEvent(i,{bubbles:!1,cancelable:!1,detail:e.detail});o.dispatchEvent(a)}}}return e.prototype.swipeToCloseChanged=function(e){this.gesture?this.gesture.enable(e):e&&this.initSwipeToClose()},e.prototype.connectedCallback=function(){Object(s["f"])(this.el)},e.prototype.present=function(){return Object(i["a"])(this,void 0,void 0,(function(){var e,t,o,r=this;return Object(i["c"])(this,(function(i){switch(i.label){case 0:if(this.presented)return[2];if(e=this.el.querySelector(".modal-wrapper"),!e)throw new Error("container is undefined");return t=Object.assign(Object.assign({},this.componentProps),{modal:this.el}),o=this,[4,Object(n["a"])(this.delegate,e,this.component,["ion-page"],t)];case 1:return o.usersElement=i.sent(),[4,Object(l["f"])(this.usersElement)];case 2:return i.sent(),Object(a["f"])((function(){return r.el.classList.add("show-modal")})),[4,Object(s["e"])(this,"modalEnter",w,x,this.presentingElement)];case 3:return i.sent(),this.swipeToClose&&this.initSwipeToClose(),[2]}}))}))},e.prototype.initSwipeToClose=function(){var e=this;if("ios"===Object(r["b"])(this)){var t=this.leaveAnimation||r["c"].get("modalLeave",v),o=this.animation=t(this.el,this.presentingElement);this.gesture=u(this.el,o,(function(){e.gestureAnimationDismissing=!0,e.animation.onFinish((function(){return Object(i["a"])(e,void 0,void 0,(function(){return Object(i["c"])(this,(function(e){switch(e.label){case 0:return[4,this.dismiss(void 0,"gesture")];case 1:return e.sent(),this.gestureAnimationDismissing=!1,[2]}}))}))}))})),this.gesture.enable(!0)}},e.prototype.dismiss=function(e,t){return Object(i["a"])(this,void 0,void 0,(function(){var o,a;return Object(i["c"])(this,(function(i){switch(i.label){case 0:return this.gestureAnimationDismissing&&"gesture"!==t?[2,!1]:(o=s["i"].get(this)||[],[4,Object(s["g"])(this,e,t,"modalLeave",v,y,this.presentingElement)]);case 1:return a=i.sent(),a?[4,Object(n["b"])(this.delegate,this.usersElement)]:[3,3];case 2:i.sent(),this.animation&&this.animation.destroy(),o.forEach((function(e){return e.destroy()})),i.label=3;case 3:return this.animation=void 0,[2,a]}}))}))},e.prototype.onDidDismiss=function(){return Object(s["h"])(this.el,"ionModalDidDismiss")},e.prototype.onWillDismiss=function(){return Object(s["h"])(this.el,"ionModalWillDismiss")},e.prototype.render=function(){var e,t=Object(r["b"])(this);return Object(a["j"])(a["c"],{"no-router":!0,"aria-modal":"true",tabindex:"-1",class:Object.assign((e={},e[t]=!0,e["modal-card"]=void 0!==this.presentingElement&&"ios"===t,e),Object(d["b"])(this.cssClass)),style:{zIndex:""+(2e4+this.overlayIndex)},onIonBackdropTap:this.onBackdropTap,onIonDismiss:this.onDismiss,onIonModalDidPresent:this.onLifecycle,onIonModalWillPresent:this.onLifecycle,onIonModalWillDismiss:this.onLifecycle,onIonModalDidDismiss:this.onLifecycle},Object(a["j"])("ion-backdrop",{visible:this.showBackdrop,tappable:this.backdropDismiss}),"ios"===t&&Object(a["j"])("div",{class:"modal-shadow"}),Object(a["j"])("div",{tabindex:"0"}),Object(a["j"])("div",{role:"dialog",class:"modal-wrapper ion-overlay-wrapper"}),Object(a["j"])("div",{tabindex:"0"}))},Object.defineProperty(e.prototype,"el",{get:function(){return Object(a["k"])(this)},enumerable:!1,configurable:!0}),Object.defineProperty(e,"watchers",{get:function(){return{swipeToClose:["swipeToCloseChanged"]}},enumerable:!1,configurable:!0}),e}(),k={ionModalDidPresent:"ionViewDidEnter",ionModalWillPresent:"ionViewWillEnter",ionModalWillDismiss:"ionViewWillLeave",ionModalDidDismiss:"ionViewDidLeave"};O.style={ios:g,md:j}},"6ad3":function(e,t,o){"use strict";o.d(t,"a",(function(){return r})),o.d(t,"b",(function(){return n}));var i=o("f530"),a=o("61cf"),r=function(e,t,o,r,n){return Object(i["a"])(void 0,void 0,void 0,(function(){var s;return Object(i["c"])(this,(function(i){switch(i.label){case 0:if(e)return[2,e.attachViewToDom(t,o,n,r)];if("string"!==typeof o&&!(o instanceof HTMLElement))throw new Error("framework delegate is missing");return s="string"===typeof o?t.ownerDocument&&t.ownerDocument.createElement(o):o,r&&r.forEach((function(e){return s.classList.add(e)})),n&&Object.assign(s,n),t.appendChild(s),[4,new Promise((function(e){return Object(a["c"])(s,e)}))];case 1:return i.sent(),[2,s]}}))}))},n=function(e,t){if(t){if(e){var o=t.parentElement;return e.removeViewFromDom(o,t)}t.remove()}return Promise.resolve()}},feca:function(e,t,o){"use strict";o.d(t,"a",(function(){return r})),o.d(t,"b",(function(){return s})),o.d(t,"c",(function(){return a})),o.d(t,"d",(function(){return l}));var i=o("f530"),a=function(e,t){return null!==t.closest(e)},r=function(e,t){var o;return"string"===typeof e&&e.length>0?Object.assign((o={"ion-color":!0},o["ion-color-"+e]=!0,o),t):t},n=function(e){if(void 0!==e){var t=Array.isArray(e)?e:e.split(" ");return t.filter((function(e){return null!=e})).map((function(e){return e.trim()})).filter((function(e){return""!==e}))}return[]},s=function(e){var t={};return n(e).forEach((function(e){return t[e]=!0})),t},d=/^[a-z][a-z0-9+\-.]*:/,l=function(e,t,o,a){return Object(i["a"])(void 0,void 0,void 0,(function(){var r;return Object(i["c"])(this,(function(i){return null!=e&&"#"!==e[0]&&!d.test(e)&&(r=document.querySelector("ion-router"),r)?(null!=t&&t.preventDefault(),[2,r.push(e,o,a)]):[2,!1]}))}))}}}]);
//# sourceMappingURL=chunk-1bff95de.ec076988.js.map