import { combineReducers } from "redux";

import userSlice from "./reducer/userSlice";

const rootReducer = combineReducers({
  user: userSlice,
});

export type RootState = ReturnType<typeof rootReducer>;
export default rootReducer;
