export const SET_WALK_ID = "SET_WALK_ID";

interface SetWalkIdAction {
  type: typeof SET_WALK_ID;
  payload: string;
}

export type WalkActionTypes = SetWalkIdAction;

export const setWalkId = (id: string): WalkActionTypes => ({
  type: SET_WALK_ID,
  payload: id,
});
