#ifndef THREADPOOL_H
#define THREADPOOL_H

#include <queue>
#include <future>
#include <functional>
#include <thread>
#include <memory>
#include <mutex>

using std::queue;
using std::packaged_task;
using std::future;
using std::function;
using std::thread;
using std::mutex;

mutex mtx;

class ThreadPool {
public:
    ThreadPool(size_t num) {
        for(size_t i = 0; i != num; ++i) {
            thread th(std::ref(*this));
        }
    }

    template<typename T, typename ... Args>
    future<T> Submit(function<T(Args...)> func, Args ... args) {
        std::function<T(void)> call_me = [=] () { return func(args...); };
        std::shared_ptr<packaged_task<T(void)>> new_task{new packaged_task<T(void)>{call_me}};
        std::function<void(void)> task_wrapper = [=](){ (*new_task)(); };
        mtx.lock();
        tasks_.push(task_wrapper);
        mtx.unlock();
        return new_task.get_future();
    }

    void operator ()() {
        while(true) {
            if(!tasks_.empty()) {
                mtx.lock();
                auto task = tasks_.back();
                tasks_.pop();
                mtx.unlock();
                task();
            }
        }
    }

    ThreadPool(ThreadPool const&) = delete;
private:
    queue<std::function<void()>> tasks_;
};

#endif // THREADPOOL_H
