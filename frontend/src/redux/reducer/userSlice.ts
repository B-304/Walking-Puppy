import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  accessToken: "",
  refreshToken: "",
  isLoggedIn: false,
  user: {
    userId: null,
    nickname: "홍길동",
    point: 0,
    emojiId: null,
    oauthProvider: "",
    saveEmoji: [1],
  },
};

const userSlice = createSlice({
  name: "user",
  initialState,

  reducers: {
    //payload의 토큰들과 유저id를 state에 저장하는 리듀서 함수입니다.
    getTokensUserId(state, action) {
      state.accessToken = action.payload.tokens.accessToken;
      state.refreshToken = action.payload.tokens.refreshToken;
      state.user.userId = action.payload.userId;
      state.user.nickname = action.payload.nickname;
    },
    // payload의 엑세스 토큰을 바탕으로 로그인 여부 state를 저장하는 리듀서 함수입니다.
    loginSuccess(state) {
      state.isLoggedIn = true;
    },

    reset(state) {
      state.accessToken = "";
      state.refreshToken = "";
      state.isLoggedIn = false;
      state.user = {
        userId: null,
        nickname: "",
        point: 0,
        emojiId: null,
        oauthProvider: "",
        saveEmoji: [1],
      };
    },
  },
});

export const userActions = userSlice.actions;
export default userSlice.reducer;
