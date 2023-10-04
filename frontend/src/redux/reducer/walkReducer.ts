import { WalkActionTypes, SET_WALK_ID } from "../action/walkAction";

interface WalkState {
  walkId: string | null;
}

const initialState: WalkState = {
  walkId: "0",
};

const walkReducer = (
  state = initialState,
  action: WalkActionTypes,
): WalkState => {
  switch (action.type) {
    case SET_WALK_ID:
      return { ...state, walkId: action.payload };
    default:
      return state;
  }
};

export default walkReducer;
