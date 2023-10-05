import { combineReducers } from "redux";

import userSlice from "./reducer/userSlice";
import walkReducer from "./reducer/walkReducer";

const rootReducer = combineReducers({
  user: userSlice,
  walk: walkReducer,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
