import type {NextRequest} from 'next/server';
import {NextResponse} from 'next/server';

export function middleware(request: NextRequest) {
    const { pathname, search } = request.nextUrl;
    // console.log('Request URL:', request.url);

    // response.headers.set('Access-Control-Allow-Origin', '*');
    // response.headers.set('Access-Control-Allow-Methods', 'GET, POST, PUT, PATCH, DELETE, OPTIONS');

    // Only proxy requests that start with `/backend/`
    if (pathname.startsWith("/backend/")) {
        const backendUrl = `http://localhost:8080${pathname.replace("/backend", "/app")}${search}`;

        return NextResponse.rewrite(backendUrl);
    }

    return NextResponse.next();
}
