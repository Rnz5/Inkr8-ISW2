// Copy to the isolated functions/lib/labEntry.js; never replace the product entry.
const nativeFetch=global.fetch;
global.fetch=(url,...args)=>{
  if(new URL(String(url)).origin!=='http://127.0.0.1:5010') throw new Error('External evaluator HTTP forbidden in lab');
  return nativeFetch(url,...args);
};
const adapter=require('./firebase/admin.js');
// CLI 15.32.1 binds admin.firestore in its proxy and loses static members.
if(adapter.FieldValue===undefined) {
  adapter.FieldValue=require('firebase-admin/firestore').FieldValue;
  console.log('Lab compatibility: genuine modular Admin FieldValue, no transaction double');
}
module.exports=require('./users/applyMeritAction.js');
if(process.env.LAB_ENABLE_EVALUATION==='true') {
  Object.assign(module.exports,require('./submissions/submissionEvaluationEngine.js'));
}
