import JSEncrypt from 'jsencrypt';
import { getPublicKey } from '@/api/system/auth';

const publicKey =
  'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCi7arytH+MJmau5lJsFt/98tJE4MTnf0aWUlrQhh5L1dfkJCai7XNGzDx1UnecVBSglDMgdmWUwOoXwQhHdb3uR9BJqH6Rj6D035lVqKyKirAYaD+PcAIZwus1HxVz/junibVT/yr6TlkpWCgS9saXGJrnX5eJjhvEjNbQjcV1vwIDAQAB';
const privateKey =
  'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKLtqvK0f4wmZq7mUmwW3/3y0kTgxOd/RpZSWtCGHkvV1+QkJqLtc0bMPHVSd5xUFKCUMyB2ZZTA6hfBCEd1ve5H0EmofpGPoPTfmVWorIqKsBhoP49wAhnC6zUfFXP+O6eJtVP/KvpOWSlYKBL2xpcYmudfl4mOG8SM1tCNxXW/AgMBAAECgYAFQj0p/HtAOrF5kRooiywR2WEOpqyXhj5w6Qz6VwhD5vQpbnGuASw4pRK8fK8H2JgaPyS2HJ9jVKWACWB37swOOnN2pavcGT/AaLX1CEItdA6mwTDJjoDLW7QCRtXm5mQx5+OwA2dCkBCZV/Fwoi9fMVq7rrR6Fhy0cMMxmoe6UQJBAOZVY8/u/JmkMW27rRleOQ829tjl26TTI7sgwE4YXVgTmC3NqFnutiJPRpIPWUnFgC4arvURH4Bsh+WSRkaaIa8CQQC1FXHH2MSklMkZ4t6L1+6gYnZIeWeWLmi2T13Bj47vRGE1cdR95/pVNxxywxq1gvMsu87CJpCpP6KehpYlwEDxAkAqfbIuF85toYak0ax7M5CfJ+qd1LmSTIkY6k/PmFsP9n1qZbga7xiWd71zEHXOUCr3VmDUQNZo4JypUzS3rZNtAkEAsLF4EJUHa8ByafvhQ3szsPPijt1HolcufZX72f8GbZm/cLLdsO1GaxgXfjO6QBrCxYeMPA39Yehh+WVB5RwvQQJARHL5Dow3b/19coHoNZun/AL5ry0xWBUzyCfGi8zN0knvesVLiRVNpXmyaxma6Yx5MwjaxDt3x/dgZ8P6/WbSUA==';

/**
 * 最长加密长度
 * @type {number}
 */
const MAX_ENCRYPT_BLOCK = 117;
/**
 * 最长解码长度
 * @type {number}
 */
const MAX_DECRYPT_BLOCK = 128;

/**
 * @description RSA加密(支持长字符加密)
 * @param data
 * @returns {Promise<{param: PromiseLike<ArrayBuffer>}|*>}
 */
export async function encryptedData(data: any) {
  let publicKey;
  const res = await getPublicKey();
  publicKey = res.data;
  if (res.data.mockServer) {
    publicKey = '';
  }
  if (publicKey === '') {
    return data;
  }
  const encrypt = new JSEncrypt();
  encrypt.setPublicKey(`-----BEGIN PUBLIC KEY-----${publicKey}-----END PUBLIC KEY-----`);
  let bufTmp: any = '';
  let hexTmp: any = '';
  let result: any = '';
  const buffer = Buffer.from(JSON.stringify(data));
  let offSet = 0;
  const inputLen = buffer.length;
  while (inputLen - offSet > 0) {
    if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
      bufTmp = buffer.slice(offSet, offSet + MAX_ENCRYPT_BLOCK);
    } else {
      bufTmp = buffer.slice(offSet, inputLen);
    }
    hexTmp = encrypt.encrypt(bufTmp.toString());
    result += atob(hexTmp);
    offSet += MAX_ENCRYPT_BLOCK;
  }
  return btoa(result);
}

/**
 * @description RSA解密(支持长字符解密)
 * @param data
 * @returns {PromiseLike<ArrayBuffer>}
 */
export function decryptedData(data: string) {
  const decrypt = new JSEncrypt();
  decrypt.setPrivateKey(`-----BEGIN RSA PRIVATE KEY-----${privateKey}-----END RSA PRIVATE KEY-----`);
  let bufTmp: any = '';
  let hexTmp: any = '';
  let result: any = '';
  const buffer = atob(data);
  let offSet = 0;
  const inputLen = buffer.length;
  while (inputLen - offSet > 0) {
    if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
      bufTmp = buffer.slice(offSet, offSet + MAX_DECRYPT_BLOCK);
    } else {
      bufTmp = buffer.slice(offSet, inputLen);
    }
    hexTmp = decrypt.decrypt(btoa(bufTmp));
    result += hexTmp;
    offSet += MAX_DECRYPT_BLOCK;
  }
  return JSON.parse(result);
}

// 加密
export async function encrypt(data: any) {
  const json = JSON.stringify(data);
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey); // 设置公钥
  return encryptor.encrypt(json); // 对数据进行加密
}

// 解密
export function decrypt(data: string) {
  const encryptor = new JSEncrypt();
  encryptor.setPrivateKey(privateKey); // 设置私钥
  return encryptor.decrypt(data); // 对数据进行解密
}
