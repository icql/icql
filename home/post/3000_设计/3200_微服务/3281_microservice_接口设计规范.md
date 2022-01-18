---
title: microservice_接口设计规范
sort: 3281
date: 2020-02-14 00:00:00
category:
- '设计'
tags:
- '微服务'
comments: true
---

https://zhishiku.casstime.com/pages/viewpage.action?pageId=31888074

## restful
restful最明显的特征，对于同一个资源的一组不同的操作，资源是以名词为核心来组织的
7个HTTP方法：GET/POST/PUT/DELETE/PATCH/HEAD/OPTIONS

## 常用规范

``` java
@Api(tags = "客户选择器接口")
public interface CustomerSelectorService {

    // region 增/删/改/查

    @ApiOperation(value = "新增客户选择器(成功：返回新增后的选择器DTO，失败：抛异常或返回null)-insert", notes = "新增客户选择器(成功：返回新增后的选择器DTO，失败：抛异常或返回null)")
    @ApiImplicitParam(name = "request", value = "新增客户选择器入参", dataType = "CustomerSelectorDTO", required = true, paramType = "body")
    @RequestMapping(value = "/selector", method = RequestMethod.POST)
    CustomerSelectorDTO insert(@RequestBody CustomerSelectorDTO request);

    @ApiOperation(value = "修改客户选择器(成功：返回修改后的选择器DTO，失败：抛异常或返回null)-update", notes = "修改客户选择器(成功：返回修改后的选择器DTO，失败：抛异常或返回null)")
    @ApiImplicitParam(name = "request", value = "修改客户选择器入参", dataType = "CustomerSelectorDTO", required = true, paramType = "body")
    @RequestMapping(value = "/selector", method = RequestMethod.PUT)
    CustomerSelectorDTO update(@RequestBody CustomerSelectorDTO request);

    @ApiOperation(value = "删除客户选择器-根据ID(成功：返回删除的选择器ID，失败：抛异常或返回空串)-deleteById", notes = "删除客户选择器-根据ID(成功：返回选择器ID，失败：抛异常或返回0)")
    @ApiImplicitParam(name = "id", value = "客户选择器id", dataType = "String", required = true, paramType = "path")
    @RequestMapping(value = "/selector/{id}", method = RequestMethod.DELETE)
    String deleteById(@PathVariable("id") String id);

    @ApiOperation(value = "查询客户选择器-根据ID-getById", notes = "查询客户选择器-根据ID")
    @ApiImplicitParam(name = "id", value = "客户选择器id", dataType = "String", required = true, paramType = "path")
    @RequestMapping(value = "/selector/{id}", method = RequestMethod.GET)
    CustomerSelectorDTO getById(@PathVariable("id") String id);

    @ApiOperation(value = "批量新增客户选择器(成功：返回新增后的选择器DTO数组，失败：抛异常或返回空List)-insertBatch", notes = "批量新增客户选择器(成功：返回新增后的选择器DTO数组，失败：抛异常或返回空List)")
    @ApiImplicitParam(name = "requests", value = "批量新增客户选择器入参", dataType = "CustomerSelectorDTO", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/insert", method = RequestMethod.POST)
    List<CustomerSelectorDTO> insertBatch(@RequestBody List<CustomerSelectorDTO> requests);

    @ApiOperation(value = "批量修改客户选择器(成功：返回修改后的选择器DTO数组，失败：抛异常或返回空List)-updateBatch", notes = "批量修改客户选择器(成功：返回修改后的选择器DTO数组，失败：抛异常或返回空List)")
    @ApiImplicitParam(name = "requests", value = "新增/修改客户选择器入参", dataType = "CustomerSelectorDTO", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/update", method = RequestMethod.POST)
    List<CustomerSelectorDTO> updateBatch(@RequestBody List<CustomerSelectorDTO> requests);

    @ApiOperation(value = "批量删除客户选择器-根据ID数组(成功：返回删除的选择器ID数组，失败：返回0或抛出异常)-deleteBatchByIds", notes = "批量删除客户选择器-根据ID数组(成功：返回删除的选择器ID数组，失败：返回0或抛出异常)")
    @ApiImplicitParam(name = "ids", value = "客户选择器id", dataType = "String", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/delete", method = RequestMethod.POST)
    List<String> deleteBatchByIds(@RequestBody List<String> ids);

    @ApiOperation(value = "批量查询客户选择器-根据ID数组-listByIds", notes = "批量查询客户选择器-根据ID数组")
    @ApiImplicitParam(name = "ids", value = "客户选择器id", dataType = "String", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/query", method = RequestMethod.POST)
    List<CustomerSelectorDTO> listByIds(@RequestBody List<String> ids);

    @ApiOperation(value = "批量查询客户选择器-根据查询入参（暂未实现，必传参数过滤结果最多2000条数据，更新时间倒序）-listByQueryRequest", notes = "批量查询客户选择器-根据过滤入参")
    @ApiImplicitParam(name = "request", value = "客户选择器过滤入参", dataType = "CustomerSelectorQueryRequest", required = true, paramType = "body")
    @RequestMapping(value = "/selectors/query/filter", method = RequestMethod.POST)
    List<CustomerSelectorDTO> listByQueryRequest(@RequestBody CustomerSelectorQueryRequest request);

    // endregion


    // region 选择过滤

    @ApiOperation(value = "选择过滤-select", notes = "选择过滤")
    @ApiImplicitParam(name = "request", value = "入参", dataType = "SelectRequest", required = true, paramType = "body")
    @RequestMapping(value = "/selector/select", method = RequestMethod.POST)
    SelectResponse select(@RequestBody SelectRequest request);

    @ApiOperation(value = "选择过滤(有10分钟缓存，选择器变动后缓存即时失效)-selectWithCache", notes = "选择过滤(有10分钟缓存，选择器变动后缓存即时失效)")
    @ApiImplicitParam(name = "request", value = "入参", dataType = "SelectRequest", required = true, paramType = "body")
    @RequestMapping(value = "/selector/select/cache", method = RequestMethod.POST)
    SelectResponse selectWithCache(@RequestBody SelectRequest request);

    @ApiOperation(value = "批量选择过滤-selectBatch", notes = "批量选择过滤")
    @ApiImplicitParam(name = "requests", value = "入参", dataType = "SelectRequest", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/select", method = RequestMethod.POST)
    List<SelectResponse> selectBatch(@RequestBody List<SelectRequest> requests);

    @ApiOperation(value = "批量选择过滤(有10分钟缓存，选择器变动后缓存即时失效)-selectBatchWithCache", notes = "批量选择过滤(有10分钟缓存，选择器变动后缓存即时失效)")
    @ApiImplicitParam(name = "requests", value = "入参", dataType = "SelectRequest", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/select/cache", method = RequestMethod.POST)
    List<SelectResponse> selectBatchWithCache(@RequestBody List<SelectRequest> requests);

    @ApiOperation(value = "定制化选择过滤-selectCustom", notes = "定制化选择过滤")
    @ApiImplicitParam(name = "request", value = "入参", dataType = "SelectCustomRequest", required = true, paramType = "body")
    @RequestMapping(value = "/selector/select/custom", method = RequestMethod.POST)
    SelectCustomResponse selectCustom(@RequestBody SelectCustomRequest request);

    @ApiOperation(value = "批量定制化选择过滤-selectCustomBatch", notes = "批量定制化选择过滤")
    @ApiImplicitParam(name = "requests", value = "入参", dataType = "SelectCustomRequest", allowMultiple = true, required = true, paramType = "body")
    @RequestMapping(value = "/selectors/select/custom", method = RequestMethod.POST)
    List<SelectCustomResponse> selectCustomBatch(@RequestBody List<SelectCustomRequest> requests);

    // endregion
}
```