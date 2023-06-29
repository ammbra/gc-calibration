import http from 'k6/http';
import {sleep} from 'k6';
import { URLSearchParams } from 'https://jslib.k6.io/url/1.0.0/index.js';

export const options = {
    summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(95)', 'p(99)', 'p(99.99)', 'count'],
    stages: [
        { duration: '30s', target: 10 }, // traffic ramp-up from 1 to 10 users over 30 seconds.
        { duration: '60s', target: 10 }, // stay at 10 users for 1 minute
        { duration: '30s', target: 0 }, // ramp-down to 0 users
    ],
};

export default function () {
    const url = new URL(`http://${__ENV.REMOTE_HOSTNAME}`);
    url.searchParams.append('name', 'sample'+(Math.floor(Math.random() * 100) + 1)+'txt');
    url.searchParams.append('interval', '1');
    url.searchParams.append('size', '300');
    const res = http.get(url.toString());
    sleep(1);
}