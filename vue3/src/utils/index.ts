/**
 * @description 格式化时间
 * @param time
 * @param cFormat
 * @returns {string|null}
 */
export function parseTime(time: string | number | Date, cFormat: string): string | null {
  if (arguments.length === 0) {
    return null;
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}';
  let date: any;
  if (typeof time === 'object') {
    date = time;
  } else {
    if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
      time = parseInt(time, 10);
    }
    if (typeof time === 'number' && time.toString().length === 10) {
      time *= 1000;
    }
    date = new Date(time);
  }
  const formatObj: any = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay(),
  };
  return format.replace(/{([ymdhisa])+}/g, (result: string | any[], key: string) => {
    let value = formatObj[key];
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value];
    }
    if (result.length > 0 && value < 10) {
      value = `0${value}`;
    }
    return value || 0;
  });
}

// 表格专用格式化
export function tableDateFormat(cellValue: any) {
  const format = '{y}-{m}-{d} {h}:{i}';
  if (!cellValue) return '';
  return parseTime(cellValue, format);
}

export function tableEnumFormatter(enumItemCodes: any, enumItemList: any) {
  let result = '';
  if (enumItemCodes === undefined) return result;
  const dataArr = enumItemCodes
    .replace(/\[/g, '')
    .replace(/\]/g, '')
    .replace(/\{/g, '')
    .replace(/\}/g, '')
    .replace(/"/g, '')
    .split(',');
  if (!enumItemList) {
    return result;
  }
  if (enumItemList === null || enumItemList === undefined || enumItemList.length === 0) {
    return result;
  }
  if (dataArr.length > 0) {
    dataArr.forEach((val: any) => {
      const findItem = enumItemList.filter((val1: { code: any }) => {
        return val1.code === val;
      });
      if (findItem.length === 0) {
        return result;
      }
      result += `${findItem[0].name},`;
      return true;
    });
  } else {
    if (enumItemList == null || enumItemList === undefined || enumItemList.length === 0) {
      return result;
    }
    const findItem = enumItemList.filter((val: { code: any }) => {
      return val.code === enumItemCodes;
    });
    if (findItem.length === 0) {
      return result;
    }
    result = findItem[0].name;
  }
  if (result !== '' && result.lastIndexOf(',') > 0) {
    result = result.substring(0, result.lastIndexOf(','));
  }
  return result;
}
