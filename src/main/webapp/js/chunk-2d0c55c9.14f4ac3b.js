(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0c55c9"],{"3f88":function(e,t,n){"use strict";n.r(t);n("4de4"),n("b0c0"),n("a4d3"),n("e01a");var c=n("7a23"),r=Object(c["g"])("Quizzes"),o={key:0,href:"tabs/quiz/new"},i=Object(c["g"])("Quizzes"),a=Object(c["g"])("Take quiz");function u(e,t,n,u,l,b){var d=Object(c["x"])("ion-title"),O=Object(c["x"])("ion-input"),j=Object(c["x"])("ion-toolbar"),f=Object(c["x"])("ion-header"),s=Object(c["x"])("ion-card-subtitle"),h=Object(c["x"])("ion-card-title"),p=Object(c["x"])("ion-button"),C=Object(c["x"])("ion-card-content"),m=Object(c["x"])("ion-card-header"),I=Object(c["x"])("ion-card"),g=Object(c["x"])("ion-item"),x=Object(c["x"])("ion-list"),z=Object(c["x"])("ion-infinite-scroll-content"),w=Object(c["x"])("ion-infinite-scroll"),_=Object(c["x"])("ion-content"),q=Object(c["x"])("ion-page");return Object(c["q"])(),Object(c["e"])(q,null,{default:Object(c["C"])((function(){return[Object(c["h"])(f,null,{default:Object(c["C"])((function(){return[Object(c["h"])(j,null,{default:Object(c["C"])((function(){return[Object(c["h"])(d,null,{default:Object(c["C"])((function(){return[r]})),_:1}),Object(c["h"])(O,{placeholder:"Filter",onIonChange:b.handleFilter,modelValue:e.filter,"onUpdate:modelValue":t[1]||(t[1]=function(t){return e.filter=t})},null,8,["onIonChange","modelValue"]),e.isTeacher?(Object(c["q"])(),Object(c["e"])("a",o,"Create quiz")):Object(c["f"])("",!0)]})),_:1})]})),_:1}),Object(c["h"])(_,{fullscreen:!0},{default:Object(c["C"])((function(){return[Object(c["h"])(f,{collapse:"condense"},{default:Object(c["C"])((function(){return[Object(c["h"])(j,null,{default:Object(c["C"])((function(){return[Object(c["h"])(d,{size:"large"},{default:Object(c["C"])((function(){return[i]})),_:1})]})),_:1})]})),_:1}),Object(c["h"])(x,null,{default:Object(c["C"])((function(){return[(Object(c["q"])(!0),Object(c["e"])(c["a"],null,Object(c["w"])(e.items,(function(t){return Object(c["q"])(),Object(c["e"])(g,{key:t},{default:Object(c["C"])((function(){return[Object(c["h"])(I,null,{default:Object(c["C"])((function(){return[Object(c["h"])(m,null,{default:Object(c["C"])((function(){return[Object(c["h"])(s,null,{default:Object(c["C"])((function(){return[Object(c["g"])(Object(c["z"])(t.name),1)]})),_:2},1024),Object(c["h"])(h,null,{default:Object(c["C"])((function(){return[Object(c["g"])(Object(c["z"])(t.description),1)]})),_:2},1024),Object(c["h"])(C,null,{default:Object(c["C"])((function(){return[e.isTeacher?(Object(c["q"])(),Object(c["e"])("a",{key:0,href:"tabs/quiz/"+t.id},"Open quiz",8,["href"])):Object(c["f"])("",!0),e.isTeacher?Object(c["f"])("",!0):(Object(c["q"])(),Object(c["e"])(p,{key:1,onClick:function(e){return b.takeQuiz(t.id)}},{default:Object(c["C"])((function(){return[a]})),_:2},1032,["onClick"]))]})),_:2},1024)]})),_:2},1024)]})),_:2},1024)]})),_:2},1024)})),128))]})),_:1}),Object(c["h"])(w,{onIonInfinite:t[2]||(t[2]=function(e){return u.loadData(e)}),threshold:"100px",id:"infinite-scroll",disabled:u.isDisabled},{default:Object(c["C"])((function(){return[Object(c["h"])(z,{"loading-spinner":"bubbles","loading-text":"Loading more data..."})]})),_:1},8,["disabled"])]})),_:1})]})),_:1})}var l=n("1da1"),b=(n("96cf"),n("159b"),n("d867")),d=n("bc3a"),O=n.n(d),j=Object(c["h"])("br",null,null,-1),f=Object(c["h"])("a",{href:"tabs/quizzes"},"Close",-1);function s(e,t,n,r,o,i){var a=Object(c["x"])("ion-title"),u=Object(c["x"])("ion-toolbar"),l=Object(c["x"])("ion-header"),b=Object(c["x"])("ion-input"),d=Object(c["x"])("ion-content"),O=Object(c["x"])("ion-page");return Object(c["q"])(),Object(c["e"])(O,null,{default:Object(c["C"])((function(){return[Object(c["h"])(l,null,{default:Object(c["C"])((function(){return[Object(c["h"])(u,null,{default:Object(c["C"])((function(){return[Object(c["h"])(a,null,{default:Object(c["C"])((function(){return[Object(c["g"])(Object(c["z"])(e.title),1)]})),_:1})]})),_:1})]})),_:1}),Object(c["h"])(d,{class:"ion-padding"},{default:Object(c["C"])((function(){return[Object(c["h"])(b,{placeholder:"Enter password",modelValue:e.pass,"onUpdate:modelValue":t[1]||(t[1]=function(t){return e.pass=t})},null,8,["modelValue"]),Object(c["h"])("a",{href:"tabs/takequiz/"+e.id+"?pass="+e.pass},"Begin",8,["href"]),j,f]})),_:1})]})),_:1})}n("a9e3");var h=Object(c["i"])({name:"Modal",props:{title:{type:String,default:"Take quiz"},id:{type:Number}},data:function(){return{pass:""}},components:{IonPage:b["r"],IonContent:b["i"],IonHeader:b["j"],IonTitle:b["y"],IonToolbar:b["z"],IonInput:b["n"]}}),p=n("6b0d"),C=n.n(p);const m=C()(h,[["render",s]]);var I=m,g={name:"Tab1",components:{IonButton:b["b"],IonHeader:b["j"],IonToolbar:b["z"],IonTitle:b["y"],IonContent:b["i"],IonPage:b["r"],IonInfiniteScroll:b["l"],IonInfiniteScrollContent:b["m"],IonItem:b["o"],IonList:b["q"],IonInput:b["n"],IonCard:b["c"],IonCardSubtitle:b["f"],IonCardTitle:b["g"],IonCardHeader:b["e"],IonCardContent:b["d"]},setup:function(){var e=Object(c["v"])(!1),t=function(){e.value=!e.value},n=function(){e.value=!0};return{isDisabled:e,toggleInfiniteScroll:t,loadData:n}},methods:{handleFilter:function(){var e=this;return Object(l["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:e.items.forEach((function(t){-1!=t.name.toLowerCase().indexOf(e.filter.toLowerCase())||-1!=t.description.toLowerCase().indexOf(e.filter.toLowerCase())?t.displayed=!0:t.displayed=!1}));case 1:case"end":return t.stop()}}),t)})))()},takeQuiz:function(e){return Object(l["a"])(regeneratorRuntime.mark((function t(){var n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,b["B"].create({component:I,componentProps:{id:e}});case 2:return n=t.sent,t.abrupt("return",n.present());case 4:case"end":return t.stop()}}),t)})))()}},data:function(){return{items:[],filter:"",isTeacher:window.localStorage.getItem("teacherJwt")}},created:function(){var e=this;O.a.get("teacherApi/quizmanagement/quizzes").then((function(t){t.data.forEach((function(t){var n={};n.name=t.name,n.description=t.description,n.id=t.id,n.displayed=!0,e.items.push(n)}))}))}};const x=C()(g,[["render",u]]);t["default"]=x}}]);
//# sourceMappingURL=chunk-2d0c55c9.14f4ac3b.js.map